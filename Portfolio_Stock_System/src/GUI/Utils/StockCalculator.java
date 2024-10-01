package GUI.Utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StockCalculator {
    public static double calculateVolatility(List<Map<String, Object>> stockHistory) {
        double sum = 0.0;
        int n = stockHistory.size();

        // 计算平均价格
        for (Map<String, Object> record : stockHistory) {
            double price = ((BigDecimal) record.get("last_sale")).doubleValue();
            sum += price;
        }
        double average = sum / n;

        // 计算平方偏差的总和
        double squaredSum = 0.0;
        for (Map<String, Object> record : stockHistory) {
            double price = ((BigDecimal) record.get("last_sale")).doubleValue();
            squaredSum += Math.pow(price - average, 2);
        }

        // 计算标准差
        return Math.sqrt(squaredSum / n);
    }

    public static double calculateLastPriceChange(List<Map<String, Object>> stockHistory) {
        TreeMap<LocalDateTime, Double> closingPrices = new TreeMap<>();

        // 确定每个交易日的收盘价
        for (Map<String, Object> record : stockHistory) {
            LocalDateTime dateTime = (LocalDateTime) record.get("last_update_time");
            double price = ((BigDecimal) record.get("last_sale")).doubleValue();

            // 纽约证券交易所和纳斯达克通常在东部时间下午4:00 PM收盘
            if (dateTime.toLocalTime().isAfter(LocalTime.of(16, 0))) {
                closingPrices.put(dateTime.toLocalDate().atTime(16, 0), price);
            }
        }

        // 获取最后两个交易日的收盘价
        if (closingPrices.size() >= 2) {
            Double[] lastTwoPrices = closingPrices.descendingMap().values().toArray(new Double[2]);
            // 计算价格变动
            return lastTwoPrices[0] - lastTwoPrices[1];
        } else {
            // 如果没有足够的数据来计算变动，返回0或抛出异常
            return 0;
        }
    }
}
