package com.eipl.amcs.utils.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.service.MemberTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.SocietyService;
import com.eipl.amcs.master.org.service.UnionService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberCreateTask extends Task<Boolean> {
    private final String societyCode;
    private String url;
    private final int cowMin;
    private final int cowMax;
    private final int buffMin;
    private final int buffMax;
    private final int sampleNo;

    private List<MemberDto> listDto = null;

    public MemberCreateTask(String societyCode, String url, int cowMin, int cowMax, int buffMin, int buffMax, int sampleNo) {
        this.societyCode = societyCode;
        this.url = url;
        this.cowMin = cowMin;
        this.cowMax = cowMax;
        this.buffMin = buffMin;
        this.buffMax = buffMax;
        this.sampleNo = sampleNo;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            updateMessage("Preparing members...");
            MainApp.identityDto = new IdentityDto();
            Society s = new Society();
            s.setCode(this.societyCode);
            MainApp.identityDto.setSociety(s);
            MemberService memberService = EmcsAppContext.getContext().getBean(MemberService.class);


            SocietyService service = EmcsAppContext.getContext().getBean(SocietyService.class);
            List<Society> list = service.findAll();

            if (list == null || list.isEmpty())
                return null;

            for (Society society : list) {
                if (society.getCode().equals(this.societyCode)) {
                    MainApp.identityDto.setSociety(society);
                }
            }

            UnionService unionService = EmcsAppContext.getContext().getBean(UnionService.class);
            List<Union> unions = unionService.findAll();
            if (unions != null || !unions.isEmpty()) {
                Union selectedUnion = unions.get(0);
                MainApp.identityDto.setUnion(selectedUnion);
            }

            MemberRepository repository = EmcsAppContext.getContext().getBean(MemberRepository.class);
            long count = repository.count();
            if (count > 0)
                return true;

            listDto = new ArrayList<>();

            MilkTypeService milkTypeService = EmcsAppContext.getContext().getBean(MilkTypeService.class);
            List<MilkType> milkTypeList = milkTypeService.findAll();


            MemberTypeService memberTypeService = EmcsAppContext.getContext().getBean(MemberTypeService.class);
            List<MemberType> memberTypeList = memberTypeService.findAll();

            GenderService genderService = EmcsAppContext.getContext().getBean(GenderService.class);
            List<Gender> genderList = genderService.findAll();

            updateMessage("Preparing members...");
            for (MilkType milkType : milkTypeList) {
                if (milkType.getName().equalsIgnoreCase("cow")) {
                    prepareMembers(milkType, memberTypeList.get(0), genderList.get(0), cowMin, cowMax);
                } else if (milkType.getName().equalsIgnoreCase("buffalo")) {
                    prepareMembers(milkType, memberTypeList.get(0), genderList.get(0), buffMin, buffMax);
                }
            }
            if (sampleNo > 0)
                prepareMembers(milkTypeList.get(0), memberTypeList.get(0), genderList.get(0), sampleNo, sampleNo);

            List<List<MemberDto>> listTemp = ListUtils.partition(listDto, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<MemberDto> memberDtos : listTemp) {
                try {
                    List<MemberImportDto> memberImportDtos = memberService.importMembers(memberDtos, CommonUtil.setIdentityHeader());
                    if (memberImportDtos == null || memberImportDtos.isEmpty())
                        continue;
                    updateMessage("Processing " + current * AppConstant.MIGRATION_LIST_SIZE + " of " + listTemp.size() * AppConstant.MIGRATION_LIST_SIZE);
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void prepareMembers(MilkType milkType, MemberType memberType, Gender gender, int min, int max) {
        for (int codeEx = min; codeEx <= max; codeEx++) {
            // Member
            Member m = new Member();
            m.setCodeEx(String.format("%04d", codeEx));
            m.setCode(MainApp.identityDto.getSociety().getCode() + m.getCodeEx());
            m.setLastName(m.getCodeEx());
            m.setFirstName("Member");
            m.setMiddleName("");
            m.setMemberType(memberType);
            m.setSociety(MainApp.identityDto.getSociety());
            m.setMobileNo("0000000000");
            m.setMilkType(milkType);

            // Member Details
            MemberDetail md = new MemberDetail();
            md.setGender(gender);
            md.setUnionCode(MainApp.identityDto.getUnion().getCode());
            md.setNumberOfCow((short) 0);
            md.setNumberOfBuffalo((short) 0);
            md.setMember(m);
            md.setDistrict(MainApp.identityDto.getSociety().getDistrict());
            md.setSubDistrict(MainApp.identityDto.getSociety().getSubDistrict());
            md.setVillage(MainApp.identityDto.getSociety().getVillage());
            md.setHamlet(MainApp.identityDto.getSociety().getHamlet());
            md.setState(MainApp.identityDto.getSociety().getState());
            m.setActive(true);

            listDto.add(new MemberDto(m, md));
        }
    }
}
