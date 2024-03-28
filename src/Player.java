enum Player {
    X, O;

    Player next() {
        return this == X ? O : X;
    }
}
