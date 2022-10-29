/**
 * @Author: Aimé
 * @Date:   2022-10-29 01:57:05
 * @Last Modified by:   Aimé
 * @Last Modified time: 2022-10-30 01:33:20
 */
const mixerName = document.getElementById("mixerName").innerText;
const lineName = document.getElementById("lineName").innerText;
const decreaseVolume = document.getElementById("decreaseVolume");
const increaseVolume = document.getElementById("increaseVolume");
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

decreaseVolume.onclick = () => {
    decrease().then(value => setVolumeValue(value));
}
increaseVolume.onclick = () => {
    increase().then(value => setVolumeValue(value));
}
getState().then(value => setVolumeValue(value));