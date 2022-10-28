package util;

// получение идентификатора увеличенного на единицу
// для классов Task, Subtask и Epic
public class Identifier {
    private int id;

    public Identifier() {
        this.id = 0;
    }

    // увеличить и вернуть id
    public int next() {
        return ++id;
    }
}