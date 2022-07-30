import os
import base64
from io import BytesIO
from PIL import Image
from flask import Flask, request, jsonify


app = Flask(__name__)
# firebaseConfig={"apiKey": "AIzaSyBmgX9F5O2exmZ1E1hiX9SkG0LHgwvX_oU",
#   "authDomain": "the-eye-25bb3.firebaseapp.com",
#   "databaseURL": "https://the-eye-25bb3-default-rtdb.firebaseio.com",
#   "projectId": "the-eye-25bb3",
#   "storageBucket": "the-eye-25bb3.appspot.com",
#   "messagingSenderId": "488125519392",
#   "appId": "1:488125519392:web:22dbe1b5eb9f43c19f4e45",
#   "measurementId": "G-Q0DPE660QV"}

# firebase=pyrebase.initialize_app(firebaseConfig)
# db=firebase.database()
# imgrequest=""
# def stream_handler(message):
#   global imgrequest
#   print(message["event"]) # put
#   print(message["path"]) # /-K7yGTTEp7O549EzTYtI
#   print(message["data"])
#   imgrequest=message["data"]

@app.route('/', methods=['POST'])
def index():
    img_str = request.json['img']
    
    binary_data = Image.open(BytesIO(base64.b64decode(img_str)))
    
    binary_data.save('kit.jpg')
    z=os.popen(f"python predict.py --img kit.jpg --model model_50000 --rnn nsteplstm --max-caption-length 30 --gpu -1 --dataset-name mscoco --out prediction.json").read()
    return jsonify({'result': z.split("\n")[-2]})

  
    # name = request.json['name']
    # job = request.json['job']
    # return jsonify({'result': name+" "+job})


@app.route('/', methods=['GET'])
def hello():
  return 'good connection'



if __name__ == "__main__":  
    app.run(debug=True, host='0.0.0.0')