package com.mesalab.tool.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class  MathUtils {

    private static final  String[] byteUnits = new String[]{"B", "KB", "MB", "GB", "TB", "PB"};
    private static final  String[] digitalUnits = new String[]{"", "K", "M"};

    /**
     *
     * <p>求和</p>
     * @param arr 数组对象
     * @return 求数组和
     */

    public static double getSum(double[] arr) {
        double sum = 0;
        for (double num : arr) {
            sum += num;
        }
        return sum;
    }

    /**
     *
     * <p>求均值</p>
     * @param arr 数组对象
     * @return 均值
     */
    public static double getMean(double[] arr) {
        return getSum(arr) / arr.length;
    }


    /**
     * 求众数
     * @param arr 数组对象
     * @return 统计学中的众数，一组数据中出现次数最多的数据
     */
    public static double getMode(double[] arr) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();

        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                map.put(arr[i], map.get(arr[i]) + 1);
            } else {
                map.put(arr[i], 1);
            }
        }
        int maxCount = 0;
        double mode = -1;
        Iterator<Double> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            double num = iter.next();
            int count = map.get(num);
            if (count > maxCount) {
                maxCount = count;
                mode = num;
            }
        }
        return mode;
    }


    /**
     * 求中位数
     * @param arr 数组对象
     * @return 返回中间数值，偶数个取平均值
     */
    public static double getMedian(double[] arr) {
        double[] tempArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(tempArr);
        if (tempArr.length % 2 == 0) {
            return (tempArr[tempArr.length >> 1] + tempArr[(tempArr.length >> 1) - 1]) / 2;
        } else {
            return tempArr[(tempArr.length >> 1)];
        }
    }


    /**
     * 求中列数
     * @param arr 数组对象
     * @return 返回中列数，极大值与极小值的平均
     */
    public static double getMidRange(double[] arr) {
        double max = arr[0], min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return (min + max) / 2;
    }

    /**
     * 求四分位数
     * @param arr 数组对象
     * @return 返回四分位数，所有数值由小到大排列并分成四等份，处于三个分割点位置的数值
     */
    public static double[] getQuartiles(double[] arr) {
        double[] tempArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(tempArr);
        double[] quartiles = new double[3];
        // 第二四分位数（中位数）
        quartiles[1] = getMedian(tempArr);
        // 求另外两个四分位数
        if (tempArr.length % 2 == 0) {
            quartiles[0] = getMedian(Arrays.copyOfRange(tempArr, 0, tempArr.length / 2));
            quartiles[2] = getMedian(Arrays.copyOfRange(tempArr, tempArr.length / 2, tempArr.length));
        } else {
            quartiles[0] = getMedian(Arrays.copyOfRange(tempArr, 0, tempArr.length / 2));
            quartiles[2] = getMedian(Arrays.copyOfRange(tempArr, tempArr.length / 2 + 1, tempArr.length));
        }
        return quartiles;
    }

    /**
     * 求极差
     * @param arr 数组对象
     * @return 返回极差，范围误差或全距(Range)，最大值与最小值之间的差距，即最大值减最小值后所得之数据
     */
    public static double getRange(double[] arr) {
        double max = arr[0], min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return max - min;
    }



    /**
     * 求四分位数极差
     * @param arr 数组对象
     * @return 四分位数极差
     */
    public static double getQuartilesRange(double[] arr) {
        return getRange(getQuartiles(arr));
    }



    /**
     * 求截断均值
     * @param arr 数组对象
     * @param p 截断量p，例如p的值为20，则截断20%（高10%，低10%）
     * @return 求截断均值
     */
    public static double getTrimmedMean(double[] arr, int p) {
        int tmp = arr.length * p / 100;
        double[] tempArr = Arrays.copyOfRange(arr, tmp, arr.length + 1 - tmp);
        return getMean(tempArr);
    }

    /**
     * 求方差
     * @param arr 数组对象
     * @return 方差，每个样本值与全体样本值的平均数之差的平方值的平均数
     */
    public static double getVariance(double[] arr) {
        double variance = 0;
        double sum = 0, sum2 = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            sum2 += arr[i] * arr[i];
        }
        variance = sum2 / arr.length - (sum / arr.length) * (sum / arr.length);
        return variance;
    }

    /**
     * 求绝对平均偏差(AAD)
     * @param arr 数组对象
     * @return 绝对平均偏差，实际值和其平均值间差的绝对值的平均，在计算需求的不同差异时使用
     */
    public static double getAbsoluteAverageDeviation(double[] arr) {
        double sum = 0;
        double mean = getMean(arr);
        for (int i = 0; i < arr.length; i++) {
            sum += Math.abs(arr[i] - mean);
        }
        return sum / arr.length;
    }


    /**
     *  求中位数绝对偏差(MAD)
     * @param arr 数组对象
     * @return
     */
    public static double getMedianAbsoluteDeviation(double[] arr) {
        double[] tempArr = new double[arr.length];
        double median = getMedian(arr);
        for (int i = 0; i < arr.length; i++) {
            tempArr[i] = Math.abs(arr[i] - median);
        }
        return getMedian(tempArr);
    }


    /**
     *  求标准差
     * @param arr 数组对象
     * @return 作为测量一组数值的离散程度之用
     */
    public static double getStandardDevition(double[] arr) {
        double sum = 0;
        double mean = getMean(arr);
        for (int i = 0; i < arr.length; i++) {
            sum += Math.sqrt((arr[i] - mean) * (arr[i] - mean));
        }
        return (sum / (arr.length - 1));
    }

    /**\
     * 格式化计算机字节为KB 、MB 、TB, 默认保留两位小数
     * @param byteSize 字节大小单位为B
     * @return 格式化后内容,byteSize <=0 ，将返回0值
     */
    public static String getFormatByteValue(long byteSize) {
        if (byteSize <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10(byteSize) / Math.log10(1024));
        if (digitGroups >= byteUnits.length) {
            digitGroups = byteUnits.length - 1;
        }
        return CommonUtil.round( byteSize / Math.pow(1024, digitGroups) ,2 ) + " " + byteUnits[digitGroups];
    }




    /**\
     * 格式化数字为K 、M , 默认保留两位小数
     * @param value 数字
     * @return 格式化后内容,value <=0 ，将返回0值
     */
    public static String getFormatDigitalValue(long value) {
        if (value <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10(value) / Math.log10(1000));
        if (digitGroups >= digitalUnits.length) {
            digitGroups = digitalUnits.length - 1;
        }
        return  new DecimalFormat("#,##0.##").format( value / Math.pow(1000, digitGroups)) + " " + digitalUnits[digitGroups];
    }



    //     138 541  643 988
    public static void main(String[] args){

        System.out.println(getFormatDigitalValue(100));


    }

















}
