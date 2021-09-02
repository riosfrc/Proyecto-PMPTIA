let blobs;

const recordButton = document.querySelector('#record');
const downloadButton = document.querySelector('#download');

recordButton.addEventListener('click', async function(e) {
  if(document.getElementById('label-recording').textContent === 'Grabar'){
	  console.log('record')
	  e.target.classList.remove('fa-circle');
	  e.target.classList.add('fa-stop-circle');
	  document.getElementById('label-recording').textContent = 'Detener';
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
  }
  else{
	console.log('stop recording')
    grabadora.stop();
    document.getElementById('label-recording').textContent = 'Grabar';
    downloadButton.disabled = false;
    e.target.classList.add('fa-circle');
    e.target.classList.remove('fa-stop-circle');
  }
});

downloadButton.addEventListener('click', function(){
	const blob = new Blob(blobs, {type: 'video/webm'});
    const btnDescargar = document.createElement('a');
    btnDescargar.href = window.URL.createObjectURL(blob);
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

document.querySelector('#start').addEventListener('click', async (e) => {
  if(e.target.classList.contains('fa-video')){
     e.target.classList.remove('fa-video');
     e.target.classList.add('fa-video-slash');
  } else{
	e.target.classList.add('fa-video');
    e.target.classList.remove('fa-video-slash');
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