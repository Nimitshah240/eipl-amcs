package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberListSaveTask extends Task<List<MemberImportDto>> {
    private final List<MemberDto> dtoList;
    private boolean fromMigration = false;

    public MemberListSaveTask(List<MemberDto> dtoList) {
        this.dtoList = dtoList;
    }

    public MemberListSaveTask(List<MemberDto> dtoList, boolean fromMigration) {
        this.dtoList = dtoList;
        this.fromMigration = fromMigration;
    }

    @Override
    protected List<MemberImportDto> call() throws Exception {
        MemberService service = EmcsAppContext.getContext().getBean(MemberService.class);

        if (fromMigration) {
            List<MemberImportDto> listRes = new ArrayList<>();
            List<List<MemberDto>> listTemp = ListUtils.partition(dtoList, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<MemberDto> memberDtos : listTemp) {
                try {
                    List<MemberImportDto> list = service.importMembers(memberDtos, CommonUtils.setIdentityHeader());
                    if (list == null || list.isEmpty())
                        continue;
                    listRes.addAll(list);
                    updateMessage("Processing " + current * AppConstant.MIGRATION_LIST_SIZE + " of " + listTemp.size() * AppConstant.MIGRATION_LIST_SIZE);
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return listRes;
        } else {
            try {
                List<MemberImportDto> list = service.importMembers(dtoList, CommonUtils.setIdentityHeader());
                if (list == null || list.isEmpty())
                    return null;
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
