package org.firstinspires.ftc.teamcode.wrappers;

public class Utils {
    @SafeVarargs
    public static <T extends Comparable<T>> T varMin(T...arguments){
        T minimum;
        minimum = arguments[0];
        for(T each : arguments){
            if(each.compareTo(minimum) < 0) minimum = each;
        }
        return minimum;
    }
}
