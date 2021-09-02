let blobs;

const cameraButton = document.getElementById('cameraButton');
const recordButton = document.getElementById('recordButton');
const downloadButton = document.getElementById('downloadButton');

const cameraIcon = document.getElementById('cameraIcon');
const recordIcon = document.getElementById('recordIcon');

function requestSessionUpload(){
	const blob = new Blob(blobs, {type: 'video/webm'});
	console.log(blob);
	let formData = new FormData();
	formData.append('file', blob, `GRABACION_${new Date().getTime()}.webm`);
	
	fetch('http://localhost:8090/tdah/session/upload', {
		method: 'POST',
		body: formData
	})
	.then(res => res.json())
	.then(data => console.log(data))
	.catch(err => console.error(err))
}

recordButton.addEventListener('click', async function(e) {
  if(recordIcon.style.color !== 'red'){
	  console.log('record');
	  blobs = [];
	  const pantallaStream = await navigator.mediaDevices.getDisplayMedia({ video:true, audio: true })
	  const usuarioStream = await navigator.mediaDevices.getUserMedia({ video: false, audio: true });
	  const context = new AudioContext();
	  const destination = context.createMediaStreamDestination();
	  if(pantallaStream.getAudioTracks().length){
	    const fuente1 = context.createMediaStreamSource(pantallaStream);
	    const f1Gain = context.createGain();
	    f1Gain.gain.value = 1;
	    fuente1.connect(f1Gain).connect(destination);
	  }
	  if(usuarioStream.getAudioTracks().length){
	    const fuente2 = context.createMediaStreamSource(usuarioStream)
	    const f2Gain = context.createGain();
	    f2Gain.gain.value = 1;
	    fuente2.connect(f2Gain).connect(destination);
	  }
	  
	  const tracks = [
	          ...pantallaStream.getVideoTracks(), 
	          ...destination.stream.getAudioTracks()
	        ];
	  const bundleStream = new MediaStream(tracks);
	  window.grabadora = new MediaRecorder(bundleStream, {mimeType: 'video/webm; codecs=vp8,opus'});
	  window.grabadora.ondataavailable = (e) => blobs.push(e.data);
	  window.grabadora.start();
	  recordIcon.style.color = 'red';
  }
  else{
	console.log('stop recording');
    window.grabadora.stop();
    recordIcon.style.color = '#333';
    downloadButton.disabled = false;
    showModal();
  }
});

downloadButton.addEventListener('click', function(){
	const blobDownload = new Blob(blobs, {type: 'video/webm'});
    const btnDescargar = document.createElement('a');
    btnDescargar.href = window.URL.createObjectURL(blobDownload);
    btnDescargar.download = `GRABACION_${new Date().getTime()}.webm`;
    btnDescargar.click();
});

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

cameraButton.addEventListener('click', async (e) => {
  	if(cameraIcon.classList.contains('fa-video')){
     	cameraIcon.classList.remove('fa-video');
     	cameraIcon.classList.add('fa-video-slash');
  	} else{
		cameraIcon.classList.add('fa-video');
    	cameraIcon.classList.remove('fa-video-slash');
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
});

// Ventana modal
const modal_container = document.getElementById('modal_container');
const close = document.getElementById('close');

const showModal = function() {
	modal_container.classList.add('show');
	window.addEventListener('click', closeModal);
};

function closeModal(e) {
	if (e.target === modal_container) {
		modal_container.classList.remove('show');
		window.removeEventListener('click', closeModal);
		requestSessionUpload();
	}
}

close.addEventListener('click', () => {
	modal_container.classList.remove('show');
});