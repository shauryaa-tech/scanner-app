from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route("/")
def home():
    return "Minimal Flask on 5001", 200

@app.route("/scrape", methods=["POST"])
def scrape():
    print("MINIMAL SCRAPE HIT")
    return jsonify({"status": "success_5001"}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=False, threaded=True)
