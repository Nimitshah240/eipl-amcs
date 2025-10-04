package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.service.BillCriteriaService;
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
@RequestMapping("bill-criteria")
public class BillCriteriaController {

    @Autowired
    private BillCriteriaService service;

    @Autowired
    private NextCodeService nextCodeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BillCriteriaController.class);

    @GetMapping
    public ResponseEntity<List<BillCriteria>> index() {
        try {
            List<BillCriteria> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<BillCriteria>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Method acts as api end point to save the bill criteria.
     *
     * @param headers
     * @param billCriteria
     * @return ResponseEntity<billCriteria>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @PostMapping
    public ResponseEntity<BillCriteria> saveBillCriteria(@RequestHeader Map<String, String> headers, @RequestBody BillCriteria billCriteria) {
        return new ResponseEntity<>(service.saveBillCriteria(billCriteria, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
    }

    /**
     * Method acts as api end point to update the bill criteria.
     *
     * @param headers
     * @param billCriteria
     * @return ResponseEntity<BillCriteria>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @PutMapping
    public ResponseEntity<BillCriteria> updateBillCriteria(@RequestHeader Map<String, String> headers, @RequestBody BillCriteria billCriteria) {
        try {
            billCriteria = service.updateBillCriteria(billCriteria, CommonUtil.getIdentityHeader(headers));
            if (billCriteria == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(billCriteria, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method acts as api end point to delete the bill head.
     *
     * @param headers
     * @param code
     * @return ResponseEntity<?>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteBillCriteria(@RequestHeader Map<String, String> headers,
                                            @PathVariable("code") String code) {
        try {
            service.delete(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as api end point to get the new generated code for the bill criteria.
     *
     * @param societyCode
     * @return ResponseEntity<String>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @GetMapping("/next-code")
    public ResponseEntity<String> nextCode(@RequestParam(name = "society", required = true) String societyCode) {
        try {
            LOGGER.info("Next bill head no for Society: {}", societyCode);
            String code = nextCodeService.getNextCode("BillCriteria", "code", societyCode, 3);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
