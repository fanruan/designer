package com.fr.env;

import com.fr.report.DesignAuthority;

public interface DesignAuthorityConfigurable {

    DesignAuthority[] getAuthorities();

    boolean updateAuthorities(DesignAuthority[] authorities) throws Exception;
}
