package com.ibox;

/**
 * Created by lenovo on 2018/6/12.
 */

public class ExpressShow {
    private String  ordernumber;
    private int imgnumber;

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getImgnumber() {
        return imgnumber;
    }

    public void setImgnumber(int imgnumber) {
        this.imgnumber = imgnumber;
    }

    public  ExpressShow(String ordernumber, int imgnumber)
    {
        this.ordernumber=ordernumber;
        this.imgnumber=imgnumber;

    }
}
