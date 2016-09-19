package ru.anrad.moonday;

/**
 * Created by Radoselskiy on 01.09.2016.
 */
public interface OnFragmentInteractionListener {
    static int DAY_HAS_BEGAN = 100;
    static int DAY_HAS_ENDED = 101;
    void onFragmentInteraction(int interactionCode);
}
