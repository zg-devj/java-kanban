package util;

import model.BaseTask;

import java.util.Comparator;

//
public class TaskComparator implements Comparator<BaseTask> {
    @Override
    public int compare(BaseTask o1, BaseTask o2) {
        //return o1.getStartTime().compareTo(o2.getStartTime());
        if (o1.getStartTime() == null) {
            return 1;
        }
        if (o1.getStartTime() != null && o2.getStartTime() == null) {
            return -1;
        }
        return o1.getStartTime().compareTo(o2.getStartTime());
    }
}
