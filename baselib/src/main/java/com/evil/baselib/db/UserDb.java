package com.evil.baselib.db;

import org.litepal.crud.DataSupport;

/**
 * @name： LiveExplorer
 * @package： com.evil.live.db
 * @author: Noah.冯 QQ:1066537317
 * @time: 18:32
 * @version: 1.1
 * @desc： TODO
 */

public class UserDb extends DataSupport {
    private String userName;
    private String company;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
