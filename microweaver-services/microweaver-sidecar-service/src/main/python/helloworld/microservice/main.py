from flask import Flask
from flask import jsonify

app = Flask(__name__)

@app.route("/health")
def health():
    return jsonify({'status' : 'UP' })

@app.route("/v1.0/hello")
def hello():
    print "This is Hello World from Flask"
    return "Hello World from Flask"

def main():
    print "Starting flask service"
    app.run(host='0.0.0.0', debug=True, port=7000)

if __name__ == "__main__":
   main()