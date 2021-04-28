public class Note {
    private boolean played;
    private int note;

    public Note() {
        this(false, 60);
    }

    public Note(boolean played, int note) {
        this.played = played;
        this.note = note;
    }

    public int getNote() {
        return note;
    }

    public boolean getPlayed() {
        return played;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public String toString() {
        return "" + note + " " + (!played ? 0 : 1) + ", ";
    }
}
