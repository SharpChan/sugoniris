package com.sugon.iris.sugondomain.enums;

/**
 * 用于表示数据库类型的枚举(FIXME 其他地方有编号和前台不匹配。以此处为标准)
 * 1 = mysql, 2 = oracle, 3 = sqlserver, 4 = mpp
 * @author cjz
 */
public enum DBType_Enum {
	/**
	 * MYSQL
	 */
    MYSQL(1), 
    /**
     * ORACLE
     */
    ORACLE(2), 
    /**
     * SQLSERVER
     */
    SQLSERVER(3), 
    /**
     * MPP
     */
    MPP(4), 
    /**
     * DAMENG
     */
    DAMENG(5), 
    /**
     * UNKNOW
     */
    UNKNOW(0);

    // 定义私有变量

    private int nCode;

    // 构造函数，枚举类型只能为私有

    private DBType_Enum(int nCode) {
        this.nCode = nCode;
    }

    @Override
    public String toString() {
        return String.valueOf(this.nCode);
    }

    public static DBType_Enum get(int index) {
        for (DBType_Enum c : DBType_Enum.values()) {
            if (c.getnCode() == index) {
                return c;
            }
        }
        return DBType_Enum.UNKNOW;
    }

    public int getnCode() {
        return nCode;
    }

    public void setnCode(int nCode) {
        this.nCode = nCode;
    }
}