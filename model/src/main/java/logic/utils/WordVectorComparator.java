package logic.utils;

import logic.model.WordVector;
import logic.model.enums.Order;

import java.util.Comparator;

public class WordVectorComparator implements Comparator<WordVector>
{
    private Order order;
    
    public WordVectorComparator()
    {
        this.order = Order.INC;
    }
    
    public WordVectorComparator(Order order)
    {
        this.order = order;
    }
    
    @Override
    public int compare(WordVector o1, WordVector o2)
    {
        return Double.compare(order.getOrder() * o1.getDistance(), o2.getDistance());
    }
    
}
