package logic.classifier.result;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClassificationResult
{
    private final static String PREDICTED_LABEL = "PREDICTED";
    private final static String ACTUAL_LABEL = "ACTUAL";
    private final Map<String, Map<String, Integer>> confusionMatrix;
    
    public ClassificationResult(Map<String, Map<String, Integer>> confusionMatrix)
    {
        this.confusionMatrix = confusionMatrix;
    }
    
    public Map<String, Map<String, Integer>> getConfusionMatrix()
    {
        return confusionMatrix;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = confusionMatrix.keySet();
        Map<String, ClassificationResultLabelSummaryData> results = new HashMap<>();
        keys.forEach((k) -> results.put(k, new ClassificationResultLabelSummaryData()));
        int allElementsCount = 0;
        int labelCellWidth = keys.stream().max(Comparator.comparing(String::length)).orElse("PREDICTED")
                                 .length();
        int valueCellWidth = confusionMatrix
            .entrySet()
            .stream()
            .mapToInt(entry -> entry.getValue().values().stream().mapToInt(val -> (int) Math.log10(val) + 1).max().orElse(Integer.valueOf(1))).max()
            .orElse(1);
        
        int cellWidth = Math.max(labelCellWidth, valueCellWidth);
        if(cellWidth < 9)
        {
            cellWidth = 9;
        }
        String labelCellFormatString = "%1$" + cellWidth + "s";
        String valueCellFormatString = "%1$" + cellWidth + "d";
        String floatValueCellFormatString = "%1$" + cellWidth + ".2f";
        sb.append(String.format(labelCellFormatString + "| " + ACTUAL_LABEL, PREDICTED_LABEL))
          .append(String.format("%n"));
        sb.append(String.format(labelCellFormatString + "|", ""));
        for(String key : keys)
        {
            sb.append(String.format(labelCellFormatString + "|", key));
        }
        sb.append("\n");
        for(String key : keys)
        {
            sb.append(String.format(labelCellFormatString + "|", key));
            confusionMatrix.get(key).forEach((label, count) -> {
                results.get(key).count += count;
                if(label.equals(key))
                {
                    results.get(key).tp += count;
                }
                else
                {
                    results.get(key).fn += count;
                }
            });
            allElementsCount += results.get(key).count;
            for(String outerMapKey : keys)
            {
                if(!key.equals(outerMapKey))
                {
                    results.get(key).fp += confusionMatrix.get(outerMapKey).get(key);
                    results.get(key).tn += confusionMatrix.get(outerMapKey).entrySet().stream().filter(
                        (entry) -> (!entry.getKey().equals(key))).mapToInt((entry) -> (entry.getValue()))
                                                          .sum();
                }
                sb.append(
                    String.format(valueCellFormatString + "|", confusionMatrix.get(outerMapKey).get(key)));
            }
            sb.append("\n");
        }
        sb.append(
            "---------------------------------------------------------------------------------------\n");
        sb.append(String.format(labelCellFormatString + "|", "COUNT"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(valueCellFormatString + "|", entry.getValue().count));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "TP"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(valueCellFormatString + "|", entry.getValue().tp));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "%TP"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(
                floatValueCellFormatString + "|",
                100.0 * (double) entry.getValue().tp / (double) entry.getValue().count
                                   ));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "TN"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(valueCellFormatString + "|", entry.getValue().tn));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "%TN"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(
                floatValueCellFormatString + "|",
                100.0 * (double) entry.getValue().tn / (double) (allElementsCount - entry
                    .getValue().count)
                                   ));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "FP"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(valueCellFormatString + "|", entry.getValue().fp));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "%FP"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(
                floatValueCellFormatString + "|",
                100.0 * (double) entry.getValue().fp / (double) (allElementsCount - entry
                    .getValue().count)
                                   ));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "FN"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(valueCellFormatString + "|", entry.getValue().fn));
        }
        sb.append(String.format("%n" + labelCellFormatString + "|", "%FN"));
        for(Map.Entry<String, ClassificationResultLabelSummaryData> entry : results.entrySet())
        {
            sb.append(String.format(
                floatValueCellFormatString + "|",
                100.0 * (double) entry.getValue().fn / (double) entry.getValue().count
                                   ));
        }
        return sb.toString();
    }
    
    private static class ClassificationResultLabelSummaryData
    {
        
        int count = 0;
        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn = 0;
    }
}
