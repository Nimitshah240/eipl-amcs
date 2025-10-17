package com.eipl.amcs.master.insurance.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailSummaryRepository;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.master.insurance.service.InsuranceMasterServiceImpl;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insurance")
public class InsuranceMasterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceMasterController.class);
    @Autowired
    private InsuranceMasterService service;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private InsuranceMasterService insuranceMasterService;
    @Autowired
    private InsuranceMasterServiceImpl insuranceMasterServiceImpl;
    @Autowired
    private InsuranceDetailSummaryRepository insuranceDetailSummaryRepository;

    @GetMapping
    public ResponseEntity<List<InsuranceMaster>> fetchInsuranceMaster() {
        try {
            List<InsuranceMaster> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<List<InsuranceDetail>> fetchInsuranceDetails(@RequestParam Integer insuranceMasterCode) {
        try {
            List<InsuranceDetail> list = service.findInsuranceDetailByInsuranceMaster(insuranceMasterCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save-summary")
    public ResponseEntity<InsuranceDetailSummary> saveInsuranceDetailSummary(@RequestHeader Map<String, String> headers, @RequestBody InsuranceDetailSummary insuranceDetailSummary) {
        try {
            return new ResponseEntity<>(service.saveDetailsSumamry(insuranceDetailSummary, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/detail")
    public ResponseEntity<InsuranceDetail> fetchInsuranceDetails(@RequestHeader Map<String, String> headers, @RequestBody InsuranceDetail insuranceDetail) {
        try {
            return new ResponseEntity<>(service.saveDetails(insuranceDetail, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/detail-finalize")
    public ResponseEntity<InsuranceDetail> fetchInsuranceDetailsFinalize(@RequestHeader Map<String, String> headers, @RequestBody InsuranceDetail insuranceDetail) {
        try {
            return new ResponseEntity<>(service.saveDetailsFinalize(insuranceDetail, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/master")
    public ResponseEntity<InsuranceMaster> fetchInsuranceMaster(@RequestHeader Map<String, String> headers, @RequestBody InsuranceMaster insuranceMaster) {
        try {
            return new ResponseEntity<>(service.saveMaster(insuranceMaster, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/detail")
    public ResponseEntity<InsuranceDetail> updateInsuranceDetails(@RequestHeader Map<String, String> headers, @RequestBody InsuranceDetail insuranceDetail) {
        try {
            return new ResponseEntity<>(service.updateDetails(insuranceDetail, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/detail-finalize")
    public ResponseEntity<InsuranceDetail> updateInsuranceDetailsFinalize(@RequestHeader Map<String, String> headers, @RequestBody InsuranceDetail insuranceDetail) {
        try {
            return new ResponseEntity<>(service.updateDetailsFinalize(insuranceDetail, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping("/save-summary")
    public ResponseEntity<InsuranceDetailSummary> updateInsuranceSummaryDetails(@RequestBody InsuranceDetailSummary insuranceDetailSummary, @RequestHeader Map<String, String> headers) {
        try {
            return new ResponseEntity<>(service.updateInsuranceSummaryDetails(insuranceDetailSummary, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/detail")
    public ResponseEntity<InsuranceDetail> deleteInsuranceDetails(@RequestHeader Map<String, String> headers, @RequestBody InsuranceDetail insuranceDetail) {
        try {
            return new ResponseEntity<>(service.deleteDetails(insuranceDetail, CommonUtil.getIdentityHeader(headers)),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/fetchInsuranceDetailCode")
    public ResponseEntity<String> getInvoiceNoNextCode(@RequestParam String code) {
        try {
            LOGGER.info("Next grn no for Society: {}", code);
            String codes = nextCodeService.getNextCode("InsuranceDetail", "insuranceDetailCode", "VLC" + "-" + code + "-", 1);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetchInsuranceDetailSrCode")
    public ResponseEntity<String> fetchInsuranceDetailSrCode(@RequestParam String code) {
        try {
            LOGGER.info("Next grn no for Society: {}", code);
            String codes = nextCodeService.getNextCode("InsuranceDetail", "srNo", code, 4);
            if (codes == null || codes.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(codes, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detailSummary")
    public ResponseEntity<InsuranceDetailSummary> fetchInsuranceSummaryDetails(@RequestParam Integer insuranceMasterCode) {
        try {
            InsuranceDetailSummary list = service.findInsuranceDetailSummaryByInsuranceMaster(insuranceMasterCode);
            if (list == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/fetchDeletedInsuranceDetails")
    public ResponseEntity<List<InsuranceDetail>> fetchDeletedInsuranceDetails(@RequestParam Integer insuranceMasterCode) {
        try {
            List<InsuranceDetail> list = service.findDeletedInsuranceDetailByInsuranceMaster(insuranceMasterCode);
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
