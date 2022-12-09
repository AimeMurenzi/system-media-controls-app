/**
 * @Author: Aimé
 * @Date:   2022-12-07 17:05:23
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-12-08 01:26:39
 */


const mixerName = document.getElementById("mixerName").innerText;
const lineName = document.getElementById("lineName").innerText;
const decreaseVolumeBTN = document.getElementById("decreaseVolume");
const increaseVolumeBTN = document.getElementById("increaseVolume");
const volumeDiv = document.getElementById("volumeDiv");


async function getState() {
    const path = encodeURI(`/api/state/mixer/${mixerName}/line/${lineName}`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json();
    console.log(data);
    return data;
}
 

async function decrease() {
    const path = encodeURI(`/api/volume/decrease/mixer/${mixerName}/line/${lineName}`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json();
    console.log(data);
    return data;
}

async function increase() {
    const path = encodeURI(`/api/volume/increase/mixer/${mixerName}/line/${lineName}`);
    const httpResponse = await fetch(path);
    const data = await httpResponse.json();
    console.log(data);
    return data;
}

function setVolumeValue(value) {
    return volumeDiv.innerText = Math.floor(value * 100) + "%";
}

decreaseVolumeBTN.onclick = () => {
    decrease().then(value => setVolumeValue(value));
}
increaseVolumeBTN.onclick = () => {
    increase().then(value => setVolumeValue(value));
}
getState().then(value => setVolumeValue(value));