package com.eipl.amcs.auth.dto;

import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IdentityDto {
    private Union union;
    private Society society;
    private Dock dock;
    private Identity identity;
}
