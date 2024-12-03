import sys
import os

# Add the `src` folder to the Python path
sys.path.append(os.path.join(os.path.dirname(__file__), "src"))

from src.server.app import create_app

app = create_app()

if __name__ == "__main__":
    app.run(debug=True)
