

package com.bhex.wallet.bh_main.validator.enums;

public enum ENTRUST_BUSI_TYPE {

    //委托
    DO_ENTRUS(1,"DO_ENTRUS"),
    //转委托
    TRANFER_ENTRUS(2,"TRANFER_ENTRUS"),
    //解委托
    RELIEVE_ENTRUS(3,"RELIEVE_ENTRUS");


    private int typeId;
    private String type;

    ENTRUST_BUSI_TYPE(int id, String mMode) {
        typeId = id;
        type = mMode;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public boolean equals (ENTRUST_BUSI_TYPE nextType) {
        return nextType.getTypeId() == typeId;
    }
}
