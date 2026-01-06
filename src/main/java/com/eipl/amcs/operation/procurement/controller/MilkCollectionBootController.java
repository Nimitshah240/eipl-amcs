package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("milk-collection")
public class MilkCollectionBootController {

    @PostMapping
    protected ResponseEntity<MilkCollection> saveCollection(@RequestParam Boolean doubleDock, @RequestBody MilkCollection milkCollection) {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            MilkCollection milkCollection1 = service.save(milkCollection, CommonUtils.setIdentityHeader());
            if (milkCollection1 != null){
                return new ResponseEntity<>(milkCollection1, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    protected ResponseEntity<MilkCollection>  updateCollection(@RequestParam Boolean doubleDock, @RequestBody MilkCollection milkCollection) {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            MilkCollection milkCollection1 = service.update(milkCollection, CommonUtils.setIdentityHeader());
            if (milkCollection1 != null){
                return new ResponseEntity<>(milkCollection1, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}