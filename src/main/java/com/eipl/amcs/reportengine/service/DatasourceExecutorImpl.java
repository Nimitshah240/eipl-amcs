package com.eipl.amcs.reportengine.service;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptDatasource;
import com.eipl.amcs.reportengine.model.RptDatasourceParameter;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.repository.RptDatasourceParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatasourceExecutorImpl
        implements DatasourceExecutor {

    @Autowired
    private final StoredProcedureExecutor storedProcedureExecutor;
    @Autowired
    private final RptDatasourceParameterRepository rptDatasourceParameterRepository;
    @Autowired
    private final ParameterResolver parameterResolver;

    @Override
    public ReportResult execute(RptDatasource datasource, Map<String, Object> parameters) {

        switch (datasource.getDatasourceType()) {

            case "SP":
                return storedProcedureExecutor.execute(datasource, parameters);
            case "CODE":
                return executeCode(datasource, parameters);
            default:
                throw new RuntimeException("Datasource not supported");
        }
    }

    private ReportResult executeCode(RptDatasource datasource, Map<String, Object> popupValues) {

        try {

            String[] definition = datasource.getDefinition().split("#");

            if (definition.length != 2) {
                throw new RuntimeException("Datasource definition must be beanName#methodName");
            }

            String className = definition[0];
            String methodName = definition[1];

            Class<?> clazz = Class.forName(className);

            Object bean = null;

            String[] beanNames = EmcsAppContext.getContext().getBeanDefinitionNames();

            for (String beanName : beanNames) {

                Object candidate = EmcsAppContext.getContext().getBean(beanName);

                Class<?> targetClass = org.springframework.aop.support.AopUtils.getTargetClass(candidate);

                if (clazz.isAssignableFrom(targetClass)) {
                    bean = candidate;
                    break;
                }
            }

            if (bean == null) {
                throw new RuntimeException("Bean not found : " + className);
            }

            List<RptDatasourceParameter> dsParameters = rptDatasourceParameterRepository.findByDatasourceDatasourceCodeOrderByParameterOrder(datasource.getDatasourceCode());

            Object[] args = buildArguments(dsParameters, popupValues);

            Method method = findMethod(clazz, methodName, args.length);

            System.out.println(bean);
            System.out.println(args.length);
            Object result = method.invoke(bean, args);

            if (!(result instanceof List<?>)) {
                throw new RuntimeException("Datasource method must return List<?>");
            }

            ReportResult reportResult = new ReportResult();
//            reportResult.setData((List<?>) result);

            return reportResult;

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    private Object[] buildArguments(List<RptDatasourceParameter> dsParameters, Map<String, Object> popupValues) {

        Object[] args = new Object[dsParameters.size()];

        for (int i = 0; i < dsParameters.size(); i++) {

            RptDatasourceParameter dsp = dsParameters.get(i);

            if (dsp.getParameterMaster() == null) {
                args[i] = dsp.getDefaultValue();
            } else {
                RptParameterMaster parameter = dsp.getParameterMaster();
                Object selectedValue = popupValues.get(parameter.getParameterMasterCode());
                args[i] = parameterResolver.resolve(parameter, selectedValue);
            }
        }

        return args;
    }

    private Method findMethod(Class<?> clazz, String methodName, int parameterCount) {
        System.out.println("Class : " + clazz.getName());
        System.out.println("Method : " + methodName);
        System.out.println("Args : " + parameterCount);
        for (Method method : clazz.getMethods()) {

            if (method.getName().equals(methodName)
                    && method.getParameterCount() == parameterCount) {

                return method;
            }
        }

        throw new RuntimeException("Method not found : " + methodName);
    }
}