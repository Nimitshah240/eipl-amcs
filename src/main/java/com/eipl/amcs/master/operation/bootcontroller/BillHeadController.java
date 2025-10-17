package com.eipl.amcs.master.operation.bootcontroller;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
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
@RequestMapping("bill-head")
public class BillHeadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillHeadController.class);
    @Autowired
    private BillHeadService service;
    @Autowired
    private NextCodeService nextCodeService;

    @GetMapping
    public ResponseEntity<List<BillHead>> index() {
        try {
            List<BillHead> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<BillHead>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method acts as api end point to save the bill head.
     *
     * @param headers
     * @param billHead
     * @return ResponseEntity<BillHead>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @PostMapping
    public ResponseEntity<BillHead> saveBillHead(@RequestHeader Map<String, String> headers, @RequestBody BillHead billHead) {
        try {
            return new ResponseEntity<>(service.saveBillHead(billHead, CommonUtil.getIdentityHeader(headers)), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as api end point to update the bill head.
     *
     * @param headers
     * @param billHead
     * @return ResponseEntity<BillHead>
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @PutMapping
    public ResponseEntity<BillHead> updateBillHead(@RequestHeader Map<String, String> headers, @RequestBody BillHead billHead) {
        try {
            billHead = service.updateBillHead(billHead, CommonUtil.getIdentityHeader(headers));
            if (billHead == null)
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(billHead, HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteBillHead(@RequestHeader Map<String, String> headers,
                                            @PathVariable("code") String code) {
        try {
            service.delete(code, CommonUtil.getIdentityHeader(headers));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as api end point to get the new generated code for the bill head.
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
            String code = nextCodeService.getNextCode("BillHead", "code", societyCode, 3);
            if (code == null || code.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(code, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
