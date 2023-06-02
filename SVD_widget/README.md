# Singular Value Decomposition widget

A small jupyter notebook using numpy and pyplot to visualize principal component analysis (PCA) via singular value decomposition (SVD). It takes in any image, converts it to greyscale, then stores greyscale values in a numpy array. SVD is run on this array to obtain factorization matrices. In the last cell, a k value can be changed to perform PCA with the most imformation-dense k eigenvectors, which are then recombined to show the refactored image.

# Demo

![input_flora](https://github.com/dustineby/Featured-Projects/assets/105869915/3c7c82cf-db71-465d-9845-687f90cf41b1)

Input image
![flora_15](https://github.com/dustineby/Featured-Projects/assets/105869915/caf61dc0-ea69-4d88-af6e-d374a31f500c)

Reconstruction, k = 15
![flora_30](https://github.com/dustineby/Featured-Projects/assets/105869915/2f1258b7-cc4d-4522-95c3-e79af9aed5b2)

Reconstruction, k = 30
![flora_80](https://github.com/dustineby/Featured-Projects/assets/105869915/5ec1cbd1-5f77-48ce-a95c-dcc1412a81bf)

Reconstruction, k = 80
