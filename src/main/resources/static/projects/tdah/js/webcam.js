'use strict';

/* globals MediaRecorder */

let mediaRecorder;
let recordedBlobs;

const errorMsgElement = document.querySelector('span#errorMsg');
const recordedVideo = document.querySelector('video#recorded');
const recordButton = document.querySelector('#record');
const downloadButton = document.querySelector('#download');


recordButton.addEventListener('click', (e) => {
	console.log(document.getElementById('label-recording').textContent);
  if (document.getElementById('label-recording').textContent === 'Grabar') {
	console.log('record')
    startRecording();
    e.target.classList.remove('fa-circle');
    e.target.classList.add('fa-stop-circle');
  } else {
	console.log('stop recording')
    stopRecording();
    document.getElementById('label-recording').textContent = 'Grabar';
    downloadButton.disabled = false;
    e.target.classList.add('fa-circle');
    e.target.classList.remove('fa-stop-circle');
  }
});

downloadButton.addEventListener('click', () => {
  const blob = new Blob(recordedBlobs, {type: 'video/mp4'});
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.style.display = 'none';
  a.href = url;
  a.download = 'test.mp4';
  document.body.appendChild(a);
  a.click();
  setTimeout(() => {
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }, 100);
});

function handleDataAvailable(event) {
  console.log('handleDataAvailable', event);
  if (event.data && event.data.size > 0) {
    recordedBlobs.push(event.data);
  }
}

function startRecording() {
  recordedBlobs = [];
  let options = {mimeType: 'video/webm;codecs=vp9,opus'};
  try {
    mediaRecorder = new MediaRecorder(window.stream, options);
  } catch (e) {
    console.error('Exception while creating MediaRecorder:', e);
    errorMsgElement.innerHTML = `Exception while creating MediaRecorder: ${JSON.stringify(e)}`;
    return;
  }

  console.log('Created MediaRecorder', mediaRecorder, 'with options', options);
  document.getElementById('label-recording').textContent = 'Detener';
  downloadButton.disabled = true;
  mediaRecorder.onstop = (event) => {
    console.log('Recorder stopped: ', event);
    console.log('Recorded Blobs: ', recordedBlobs);
  };
  mediaRecorder.ondataavailable = handleDataAvailable;
  mediaRecorder.start();
  console.log('MediaRecorder started', mediaRecorder);
}

function stopRecording() {
  mediaRecorder.stop();
}

function handleSuccess(stream) {
  recordButton.disabled = false;
  console.log('getUserMedia() got stream:', stream);
  window.stream = stream;

  const gumVideo = document.querySelector('video#gum');
  gumVideo.srcObject = stream;
}

async function init(constraints) {
  try {
    const stream = await navigator.mediaDevices.getUserMedia(constraints);
    handleSuccess(stream);
  } catch (e) {
    console.error('navigator.getUserMedia error:', e);
    errorMsgElement.innerHTML = `navigator.getUserMedia error:${e.toString()}`;
  }
}

document.querySelector('#start').addEventListener('click', async (e) => {
  if(e.target.classList.contains('fa-video')){
     e.target.classList.remove('fa-video');
     e.target.classList.add('fa-video-slash');
     document.querySelector('.activar-camara__label').textContent = "Apagar camara";
  } else{
	e.target.classList.add('fa-video');
    e.target.classList.remove('fa-video-slash');
    document.querySelector('.activar-camara__label').textContent = "Activar camara";
}
  const hasEchoCancellation = true;
  const constraints = {
    audio: {
      echoCancellation: {exact: hasEchoCancellation}
    },
    video: {
      width: 1280, height: 720
    }
  };
  console.log('Using media constraints:', constraints);
  await init(constraints);
  document.querySelector('.gum-icon').classList.remove('fa-video-slash');
});