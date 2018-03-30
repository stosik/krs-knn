package logic.model.enums;

public enum Order
{
    INC(1),
    DEC(-1);
    
    private int order;
    
    Order(int order) {
        this.order = order;
    }
    
    public int getOrder() {
        return order;
    }
}
