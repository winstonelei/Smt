package com.github.search;

public class BinarySearch {
    /*
     * 循环实现二分查找算法arr 已排好序的数组x 需要查找的数-1 无法查到数据
     * 1.二分查找又称折半查找，它是一种效率较高的查找方法。
       2.二分查找要求：（1）必须采用顺序存储结构 （2）.必须按关键字大小有序排列
       3.原理：将数组分为三部分，依次是中值（所谓的中值就是数组中间位置的那个值）前，中值，中值后；将要查找的值和数组的中值进行比较，若小于中值则在中值前 面找，若大于中值则在中值后面找，等于中值时直接返回。然后依次是一个递归过程，将前半部分或者后半部分继续分解为三部分。
       4.实现：二分查找的实现用递归和循环两种方式
     *
     */
    public static int binarySearch(int[] arr, int x) {
        int low = 0;   
        int high = arr.length-1;   
        while(low <= high) {   
            int middle = (low + high)/2;   
            if(x == arr[middle]) {   
                return middle;   
            }else if(x <arr[middle]) {   
                high = middle - 1;   
            }else {   
                low = middle + 1;   
            }  
        }  
        return -1;  
    }
    //递归实现二分查找
    public static int binarySearch(int[] dataset,int data,int beginIndex,int endIndex){    
           int midIndex = (beginIndex+endIndex)/2;    
           if(data <dataset[beginIndex]||data>dataset[endIndex]||beginIndex>endIndex){  
               return -1;    
           }  
           if(data <dataset[midIndex]){    
               return binarySearch(dataset,data,beginIndex,midIndex-1);    
           }else if(data>dataset[midIndex]){    
               return binarySearch(dataset,data,midIndex+1,endIndex);    
           }else {    
               return midIndex;    
           }    
       }   

    public static void main(String[] args) {
        int[] arr = { 6, 12, 33, 87, 90, 97, 108, 561 };
        long begtinTime = System.currentTimeMillis();
        System.out.println("循环查找：" + (binarySearch(arr, 87) + 1));
        long endTime = System.currentTimeMillis();
        System.out.println("循环查找耗时="+(endTime-begtinTime));

        long begtinTime1 = System.currentTimeMillis();
        System.out.println("递归查找"+binarySearch(arr,33,0,arr.length-1));
        long endTime1 = System.currentTimeMillis();
        System.out.println("递归查找耗时="+(endTime1-begtinTime1));
    }
}