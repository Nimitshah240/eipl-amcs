package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.operation.dto.MemberDownloadDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.network.RealTimeMultipleResponse;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.utils.AppConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eipl.amcs.utils.AppConstant.baseUrlRealTime;

public class MemberDownloadTask extends Task<Object> {

    private Identity identity;

    public MemberDownloadTask(Identity identity) {
        this.identity = identity;
    }

    protected Object call() {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            MemberRepository memberRepository = EmcsAppContext.getContext().getBean(MemberRepository.class);
            MemberDetailRepository memberDetailRepository = EmcsAppContext.getContext().getBean(MemberDetailRepository.class);
            MilkTypeRepository milkTypeRepository = EmcsAppContext.getContext().getBean(MilkTypeRepository.class);
            long count = memberRepository.count();
            if (count > 0) {
                return false;
            }
            List<MilkType> milkTypeList = milkTypeRepository.findAll(Sort.by("code").ascending());

//        Map<String,String> map = new HashMap<>(MainApp.identityDto.getSociety().getCode());

            Map<String, String> map = new HashMap<>();
            map.put("dcsCode", identity.getSocietyRefCode());

            String url = baseUrlRealTime + AppConstant.UrlPath.MEMBER_DOWNLOAD;

            RealTimeRequest<Map<String, String>> requestPayloadStartup = new RealTimeRequest<>(identity.getSocietyCode(), identity.getToken(), map);
            requestPayloadStartup.setOrganizationCode(identity.getSocietyRefCode());
            requestPayloadStartup.setContent(map);
            ResponseEntity<RealTimeMultipleResponse> responseStartUp = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayloadStartup), RealTimeMultipleResponse.class);

            RealTimeMultipleResponse respBody = responseStartUp.getBody();
            ObjectMapper mapper = new ObjectMapper();

            List<MemberDownloadDto> list = mapper.convertValue(
                    respBody.getData(),
                    new TypeReference<List<MemberDownloadDto>>() {
                    }
            );

            List<Member> memberList = new ArrayList<>();
            List<MemberDetail> memberDetailList = new ArrayList<>();

            for (MemberDownloadDto dto : list) {
                Member member = new Member();
                MemberDetail memberDetail = new MemberDetail();

                member.setCode(dto.getRefCode());
                member.setCodeEx(dto.getExMemberCode());
                member.setFirstName(dto.getMemberName());
                member.setMiddleName(dto.getFatherName());
                member.setLastName(dto.getSurname());
                member.setFirstNameLocal(dto.getLocalName());
                member.setMiddleNameLocal(dto.getLocalFatherNamae());
                member.setLastNameLocal(dto.getLocalSurname());
                member.setMobileNo(dto.getMobileNo());
                member.setCreditLimit(new BigDecimal(0));
                member.setMilkType(milkTypeList.get((Integer.parseInt(dto.getAnimalTypeCode()) - 1)));
                memberList.add(member);

                memberDetail.setCode(dto.getRefCode());
                memberDetail.setPaymentMode(dto.getPaymentMode() != null ? Short.valueOf(dto.getPaymentMode()) : null);
                memberDetail.setAddress(dto.getAddress());
                memberDetail.setPincode(dto.getPincode());
                memberDetail.setAccountNo(dto.getBankAccountNo());
                memberDetail.setIfsc(dto.getIfsc());
                memberDetail.setAadharNo(dto.getAdharNo());
                memberDetail.setPanNo(dto.getPanNo());
                memberDetail.setEmail(dto.getEmail());
                memberDetail.setRegistrationDate(dto.getRegistrationDate() != null ? LocalDate.parse(dto.getRegistrationDate()) : null);
                memberDetail.setNumberOfBuffalo(dto.getNoOfBuffalo() != null ? Short.valueOf(dto.getNoOfBuffalo()) : null);
                memberDetail.setUnionCode(dto.getUnionCode());
                memberDetail.setMember(member);
                memberDetailList.add(memberDetail);
            }
            if (!memberList.isEmpty()) {
                List<Member> memberList1 = memberRepository.saveAll(memberList);
                boolean needToCreateMember = memberList1.isEmpty();
                memberDetailRepository.saveAll(memberDetailList);
                return needToCreateMember;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
