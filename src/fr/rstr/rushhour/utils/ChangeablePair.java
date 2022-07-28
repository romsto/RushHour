package fr.rstr.rushhour.utils;

public class ChangeablePair<P, S> {

    private P first;
    private S second;

    /**
     * Two variables pair mutable
     *
     * @param first  variable 1
     * @param second variable 2
     */
    public ChangeablePair(P first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Create a new pair
     *
     * @param p   v1
     * @param s   v2
     * @param <P> type v1
     * @param <S> type v2
     * @return new pair
     */
    public static <P, S> ChangeablePair<P, S> of(P p, S s) {
        return new ChangeablePair<>(p, s);
    }

    /**
     * Get first element
     *
     * @return v1
     */
    public P getFirst() {
        return this.first;
    }

    /**
     * Update first element
     *
     * @param premier v1
     */
    public void setFirst(P premier) {
        this.first = premier;
    }

    /**
     * Get second element
     *
     * @return v2
     */
    public S getSecond() {
        return this.second;
    }

    /**
     * Update second element
     *
     * @param second v2
     */
    public void setSecond(S second) {
        this.second = second;
    }
}
