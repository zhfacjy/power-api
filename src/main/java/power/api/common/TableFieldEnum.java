package power.api.common;

public enum TableFieldEnum {

    LOOP_NAME("回路名称", "loop_name"),
    COLECTION_TIME("收集时间", "collection_time"),
    P_A("Pa", "p_a"),
    P_B("Pb", "p_b"),
    P_C("Pc", "p_c"),
    P("P", "p"),
    I_A("Ia", "i_a"),
    I_B("Ib", "i_b"),
    I_C("Ic", "i_c"),
    U_A("Ua", "u_a"),
    U_B("Ub", "u_b"),
    U_C("Uc", "u_c"),
    U_AB("Uab", "u_ab"),
    U_BC("Ubc", "u_bc"),
    U_CA("Uca", "u_ca"),
    F_R("Fr", "f_r"),
    P_FA("Pfa", "p_fa"),
    P_FB("Pfb", "p_fb"),
    P_FC("Pfc", "p_fc"),
    P_F("Pf", "p_f"),
    Q_A("Qa", "q_a"),
    Q_B("Qb", "q_b"),
    Q_C("Qc", "q_c"),
    Q("Q", "q"),
    S_A("Sa", "s_a"),
    S_B("Sb", "s_b"),
    S_C("Sc", "s_c"),
    S("S", "s"),
    I_UNB("IUnB", "iunb"),
    U_UNB("UUnB", "uunb");

    private final String name;
    private final String value;

    TableFieldEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    //根据key获取枚举
    public static TableFieldEnum getEnumByName(String key) {
        if (null == key) {
            return null;
        }
        for (TableFieldEnum temp : TableFieldEnum.values()) {
            if (temp.getName().equals(key)) {
                return temp;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
