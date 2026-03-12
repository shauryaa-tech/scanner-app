from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route("/")
def home():
    return "Minimal Flask Running", 200

@app.route("/scrape", methods=["POST"])
def scrape():
    print("MINIMAL SCRAPE HIT")
    return jsonify({"status": "minimal_success"}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True, threaded=True)
