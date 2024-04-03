enum Player {
    X("Player X"),
    O("Player O");

    private final String displayName;

    Player(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    Player next() {
        return this == X ? O : X;
    }
}
