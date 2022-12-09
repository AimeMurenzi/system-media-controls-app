/**
 * @Author: Aimé
 * @Date:   2022-11-03 14:44:02
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 01:26:29
 */

const volumeDiv = document.getElementById("volumeDiv");
const decreaseVolumeBTN = document.getElementById("decreaseVolumeBTN");
const muteBTN = document.getElementById("muteBTN");
const increaseVolumeBTN = document.getElementById("increaseVolumeBTN");
const previousBTN = document.getElementById("previousBTN");
const playPauseBTN = document.getElementById("playPauseBTN"); 
const nextBTN = document.getElementById("nextBTN");
 
async function gatMainState() {
    const path = encodeURI(`/api/state/mixer/main/line`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json(); 
    return data;
}

async function decrease() {
    const path = encodeURI(`/api/volume/decrease`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json(); 
    return data;
}

async function increase() {
    const path = encodeURI(`/api/volume/increase`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json(); 
    return data;
}
async function mute() {
    const path = encodeURI(`/api/volume/mute`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json(); 
    return data;
}
async function next() {
    const path = encodeURI(`/api/media/next`);
   return await fetch(path); 
}
async function previous() {
    const path = encodeURI(`/api/media/previous`);
   return await fetch(path); 
}
async function play() {
    const path = encodeURI(`/api/media/play`);
   return await fetch(path); 
}
function setVolumeValue(value) {
    return volumeDiv.innerText = Math.floor(value * 100) + "%";
}

decreaseVolumeBTN.onclick = () =>decrease().then(value => setVolumeValue(value));
increaseVolumeBTN.onclick = () =>increase().then(value => setVolumeValue(value));
muteBTN.onclick = () => mute().then(value => setVolumeValue(value));
previousBTN.onclick = () =>previous();
playPauseBTN.onclick = () => play();
nextBTN.onclick = () => next(); 
gatMainState().then(value => setVolumeValue(value));