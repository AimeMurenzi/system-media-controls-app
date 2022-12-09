/**
 * @Author: Aimé
 * @Date:   2022-12-04 18:39:25
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 22:30:30
 */
#include <jni.h>
#include <stdio.h>
#include <X11/Xlib.h>
#include <X11/extensions/XTest.h>
#include "MediaKey.h"
//resource fro more keys: https://cgit.freedesktop.org/xorg/proto/x11proto/tree/XF86keysym.h
#define MU 0x1008FF12 /* Mute sound from the system (XF86XK_AudioMute)*/
#define PL 0x1008FF14 /* Start playing of audio >   (XF86XK_AudioPlay)*/
#define PA 0x1008FF31 /* Pause audio playing        (XF86XK_AudioPause)*/
#define ST 0x1008FF15 /* Stop playing audio         (XF86XK_AudioStop)*/
#define PR 0x1008FF16 /* Previous track             (XF86XK_AudioPrev)*/
#define NE 0x1008FF17 /* Next track                 (XF86XK_AudioNext)*/
#define LV 0x1008FF11 /* Volume control down        (XF86XK_AudioLowerVolume)*/
#define RV 0x1008FF13 /* Volume control up          (XF86XK_AudioRaiseVolume)*/

void press(int key)
{
    // Connect to X
    Display *display;
    display = XOpenDisplay(NULL);
    // Get the keycode
    unsigned int keycode = XKeysymToKeycode(display, key);
    //printf("Simulating keycode %d press\n", keycode);
    // Simulate key press
    XTestFakeKeyEvent(display, keycode, 1, 0);
    // Simulate key release
    XTestFakeKeyEvent(display, keycode, 0, 0);
    // Clear the X buffer and sends the key press
    XFlush(display);
    // Disconnect from X
    XCloseDisplay(display);
}

JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_mute(JNIEnv *, jclass)
{
    press(MU);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_play(JNIEnv *, jclass)
{
    press(PL);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_pause(JNIEnv *, jclass)
{
    press(PA);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_stop(JNIEnv *, jclass)
{
    press(ST);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_next(JNIEnv *, jclass)
{
    press(NE);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_previous(JNIEnv *, jclass)
{
    press(PR);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_lowerVolume(JNIEnv *, jclass)
{
    press(LV);
}
JNIEXPORT void JNICALL Java_be_freeaime_systemmediacontrol_service_MediaKey_raiseVolume(JNIEnv *, jclass)
{
    press(RV);
}
