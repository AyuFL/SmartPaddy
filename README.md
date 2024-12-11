## **🌾 Project Overview**  
**SmartPaddy** utilizes Convolutional Neural Networks (CNNs) to classify rice leaf conditions into six categories:  
1. **Healthy**  
2. **Blight**  
3. **Brown Spot**  
4. **Blast**  
5. **Blight**  
6. **Tungro**  

The application processes images captured by users and provides disease identification along with actionable suggestions for mitigation.  

---

## **🔄 Workflow**  
1. **Data Preparation**  
  - Collect rice leaf images (6265 files belonging to 6 classes:
    {'blast': 1033, 'blight': 1063, 'brownspot': 1034, 'healthy': 1045, 'hispa': 1045, 'tungro': 1045}).
  - Load and preprocess the dataset using tf.keras.preprocessing.image_dataset_from_directory.
  - Automatically resize images to the target size (img_height, img_width).
  - Split the dataset into training (80%), validation (10%), and testing (10%) subsets.
  - Optimize data loading by enabling prefetching with tf.data.AUTOTUNE to improve pipeline performance. 

2. **Model Development**  
   - Use a custom CNN architecture with 4 convolutional blocks for classification, designed to effectively extract features from rice leaf images  
   - Evaluate performance using metrics like accuracy, precision, recall, and F1-score.  

3. **Deployment**  
   - Integrate the model into a mobile application for real-time inference.  
   - Use cloud services to handle backend computation and storage.  

4. **Testing & Monitoring**  
   - Continuously test with real-world data from farmers.  
   - Monitor model performance and update periodically to account for new diseases.

---

## **📁 Repository Structure**  
```
SmartPaddy/
├── Rice Leaf/               # Raw rice leaf images used for training and testing
├── data_test/               # Test dataset for evaluating model performance
├── SmartPaddy/              # Core machine learning scripts and resources
├── models/                  # Trained models in HDF5 and TensorFlow Lite formats
│   ├── Model.tflite             # Optimized model for deployment on mobile devices
│   ├── SmartPaddy_final_.h5     # First iteration of the final trained model
│   └── SmartPaddy_final_v2.h5   # Improved version of the final model
├── notebooks/              # Jupyter notebooks for model development and testing
│   ├── Transfer Learning.ipynb  # Transfer learning experiments
│   ├── inferensi.ipynb          # Inference testing and pipeline validation
│   ├── proyekFinal.ipynb        # Main project notebook for training and evaluation
│   └── proyekFinal_v3.ipynb     # Refined version of the main project notebook
├── requirements.txt        # Dependencies and libraries for the project
└── README.md               # Project overview and instructions

```

---

## **🚀 Getting Started**  
### Prerequisites  
  - ﻿tensorflow==2.18.0
  - numpy==2.0.2
  - pandas==2.2.2
  - matplotlib==3.9.2
  - scikit-learn==1.5.2

### Installation  
1. Clone the repository:  
   ```bash
   git clone https://github.com/your-repo-name/SmartPaddy.git](https://github.com/AyuFL/SmartPaddy.git
   cd SmartPaddy
   ```
2. Install dependencies:  
   ```bash
   pip install -r requirements.txt
   ```
