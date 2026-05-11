package vn.edu.tdtu.edocument.validation;

public class ValidationChainFactory {

    public static ValidationHandler createStandardChain() {
        ValidationHandler basic = new BasicValidationHandler();
        ValidationHandler security = new SecurityValidationHandler();
        ValidationHandler integrity = new IntegrityValidationHandler();

        // Cấu hình thứ tự
        basic.setNext(security).setNext(integrity);

        return basic;
    }
}