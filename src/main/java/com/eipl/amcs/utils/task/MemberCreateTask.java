package com.eipl.amcs.utils.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.dto.MemberDetail;
import com.eipl.amcs.master.operation.dto.MemberDto;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberCreateTask extends Task<Boolean> {
    private String societyCode;
    private String url;
    private int cowMin, cowMax, buffMin, buffMax;
    private int sampleNo;

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

//            Union union = new Union();
//            union.setCode("101");
//            MainApp.identityDto.setUnion(union);
            RestTemplate restTemplate1 = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url1 = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + AppConstant.UrlPath.SOCIETY;
            ResponseEntity<Society[]> response1 = restTemplate1.getForEntity(url1, Society[].class);
            if (response1.getStatusCode() != HttpStatus.OK)
                return null;

            List<Society> list = Arrays.asList(response1.getBody());
            for (Society society : list) {
                if (society.getCode().equals(this.societyCode)) {
                    MainApp.identityDto.setSociety(society);
                }
            }
            RestTemplate restTemplateUnion = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String unionUrl = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + AppConstant.UrlPath.UNION;
            ResponseEntity<Union[]> unionResponse = restTemplateUnion.getForEntity(unionUrl, Union[].class);

            if (unionResponse.getStatusCode() == HttpStatus.OK && unionResponse.getBody() != null) {
                List<Union> unions = Arrays.asList(unionResponse.getBody());

                if (!unions.isEmpty()) {
                    Union selectedUnion = unions.get(0);
                    MainApp.identityDto.setUnion(selectedUnion);
                }
            }


            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + "/members/count";
            ResponseEntity<Long> response = restTemplate.getForEntity(url, Long.class);
            if (response.getStatusCode() != HttpStatus.OK)
                return false;
            Long count = response.getBody();
            if (count.longValue() > 0)
                return true;

            listDto = new ArrayList<>();

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + AppConstant.UrlPath.MILK_TYPE;
            ResponseEntity<MilkType[]> response2 = restTemplate.getForEntity(url, MilkType[].class);
            MilkType[] milkTypeArr = response2.getBody();

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + AppConstant.UrlPath.MEMBERTYPE;
            ResponseEntity<MemberType[]> response3 = restTemplate.getForEntity(url, MemberType[].class);
            MemberType[] memberTypeArr = response3.getBody();

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + AppConstant.UrlPath.GENDER;
            ResponseEntity<Gender[]> response4 = restTemplate.getForEntity(url, Gender[].class);
            Gender[] genderArr = response4.getBody();

            updateMessage("Preparing members...");
            for (MilkType milkType : milkTypeArr) {
                if (milkType.getName().equalsIgnoreCase("cow")) {
                    prepareMembers(milkType, memberTypeArr[0], genderArr[0], cowMin, cowMax);
                } else if (milkType.getName().equalsIgnoreCase("buffalo")) {
                    prepareMembers(milkType, memberTypeArr[0], genderArr[0], buffMin, buffMax);
                }
            }
            if (sampleNo > 0)
                prepareMembers(milkTypeArr[0], memberTypeArr[0], genderArr[0], sampleNo, sampleNo);

            List<List<MemberDto>> listTemp = ListUtils.partition(listDto, AppConstant.MIGRATION_LIST_SIZE);
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, this.url) + AppConstant.UrlPath.MEMBER + "/import";
            int current = 1;
            for (List<MemberDto> memberDtos : listTemp) {
                try {
                    ResponseEntity<MemberImportDto[]> response5 = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(memberDtos), MemberImportDto[].class);

                    if (response5 == null || response5.getStatusCode() != HttpStatus.OK)
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
