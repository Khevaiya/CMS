# DataVista APP

## Overview
This project consists of a *Python (FastAPI) backend* and a *React frontend*. The application allows users to upload a CSV file, store it in a database, process it based on user prompts, and generate a graph based on the results.

## Features
1. *CSV Upload* - Users can upload a CSV file via the frontend.
2. *Database Storage* - The uploaded file is stored in a database.
3. *Prompt Processing* - Users provide prompts to analyze the stored data.
4. *Result Generation* - The system processes the prompts and returns results.
5. *Graph Generation* - Users can generate visual representations of the results.

## Tech Stack
- *Backend:* Python (FastAPI), PostgreSQL (or MongoDB as per configuration)
- *Frontend:* React.js
- *Database:* PostgreSQL / MongoDB
- *Deployment:* Docker, Azure Kubernetes Service (AKS)

## Installation
### Prerequisites
Ensure you have the following installed:
- Python 3.10+
- Node.js & npm/yarn
- PostgreSQL / MongoDB
- Docker (for deployment)

### Backend Setup
1. Clone the repository:
   sh
   git clone https://github.com/yourusername/csv-processing-app.git
   cd csv-processing-app/backend
   
2. Create a virtual environment:
   sh
   python -m venv venv
   source venv/bin/activate   # For Linux/macOS
   venv\Scripts\activate     # For Windows
   
3. Install dependencies:
   sh
   pip install -r requirements.txt
   
4. Set up environment variables in .env:
   env
   DATABASE_URL=postgresql://user:password@localhost:5432/csvdb
   
5. Run the FastAPI server:
   sh
   uvicorn main:app --reload
   

### Frontend Setup
1. Navigate to the frontend directory:
   sh
   cd ../frontend
   
2. Install dependencies:
   sh
   npm install  # or yarn install
   
3. Start the React development server:
   sh
   npm start  # or yarn start
   

## API Endpoints
### Upload CSV
- *Endpoint:* POST /upload
- *Description:* Accepts a CSV file and stores it in the database.

### Get Prompt Result
- *Endpoint:* POST /process
- *Description:* Accepts user input and processes the stored data accordingly.

### Generate Graph
- *Endpoint:* POST /generate-graph
- *Description:* Generates a graphical representation based on the processed results.

## Usage
1. Open the frontend in your browser (http://localhost:3000).
2. Click the *Upload CSV* button and select a CSV file.
3. After successful upload, enter a *prompt* to analyze the data.
4. View the result of the processed prompt.
5. Click the *Generate Graph* button to visualize the result.

## Deployment
### Using Docker
1. Build and run the backend:
   sh
   docker build -t csv-backend .
   docker run -p 8000:8000 csv-backend
   
2. Build and run the frontend:
   sh
   docker build -t csv-frontend .
   docker run -p 3000:3000 csv-frontend
   

### Deploying on Azure Kubernetes Service (AKS)
1. Push Docker images to Azure Container Registry (ACR).
2. Create AKS cluster and deploy the application.
3. Configure Ingress for secure API access.

## Future Improvements
- Implement authentication and authorization.
- Support for multiple file formats.
- Enhanced graph customization.

## License
This project is licensed under the MIT License.

## Author
[Your Name](https://github.com/yourusername)
