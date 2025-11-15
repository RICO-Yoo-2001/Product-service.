# Product Service API - Postman Endpoints

## Base URL
```
http://localhost:8080
```

## Swagger UI
```
http://localhost:8080/swagger-ui.html
```

## API Documentation
```
http://localhost:8080/api-docs
```

---

## 1. Add Product - POST

### Option A: Add Product with Base64 Image (JSON)

**Endpoint:** `POST /api/products/add`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "prodName": "Laptop",
  "prodCode": "PROD001",
  "prodDescription": "High-performance laptop with latest specifications",
  "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
  "prodCost": 999.99
}
```

### Option B: Add Product with File Upload (Multipart)

**Endpoint:** `POST /api/products/add-with-image`

**Headers:**
```
Content-Type: multipart/form-data
```

**Request Body (Form Data):**
- `prodName`: "Laptop" (required)
- `prodCode`: "PROD001" (required)
- `prodDescription`: "High-performance laptop with latest specifications" (optional)
- `prodImage`: [Select File] - Image file (JPEG, PNG, GIF, WEBP - max 5MB) (optional)
- `prodCost`: 999.99 (required)

**Note:** The image file will be automatically converted to Base64 and stored in the database.

**Response (201 Created):**
```json
{
  "statusCode": 201,
  "statusMessage": "Created",
  "description": "Product added successfully",
  "payload": {
    "prodId": 1,
    "prodName": "Laptop",
    "prodCode": "PROD001",
    "prodDescription": "High-performance laptop with latest specifications",
    "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
    "prodCost": 999.99
  },
  "timestamp": "2024-01-15 10:30:00"
}
```

---

## 2. Update Product - POST

### Option A: Update Product with Base64 Image (JSON)

**Endpoint:** `POST /api/products/update`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "prodId": 1,
  "prodName": "Gaming Laptop",
  "prodCode": "PROD001",
  "prodDescription": "Updated high-performance gaming laptop",
  "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
  "prodCost": 1299.99
}
```

**Note:** The `prodId` in the request body is used only to identify which product to update. The product ID cannot be changed - it will always remain the same as the original product ID.

### Option B: Update Product with File Upload (Multipart)

**Endpoint:** `POST /api/products/update-with-image`

**Headers:**
```
Content-Type: multipart/form-data
```

**Request Body (Form Data):**
- `prodId`: 1 (required)
- `prodName`: "Gaming Laptop" (required)
- `prodCode`: "PROD001" (required)
- `prodDescription`: "Updated high-performance gaming laptop" (optional)
- `prodImage`: [Select File] - Image file (JPEG, PNG, GIF, WEBP - max 5MB) (optional)
- `prodCost`: 1299.99 (required)

**Note:** The image file will be automatically converted to Base64 and stored in the database. The `prodId` cannot be changed.

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "statusMessage": "OK",
  "description": "Product updated successfully",
  "payload": {
    "prodId": 1,
    "prodName": "Gaming Laptop",
    "prodCode": "PROD001",
    "prodDescription": "Updated high-performance gaming laptop",
    "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
    "prodCost": 1299.99
  },
  "timestamp": "2024-01-15 11:45:00"
}
```

---

## 3. Search Products - GET

### 3.1 Search by Product Name

**Endpoint:** `GET /api/products/search?productName=Laptop`

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "statusMessage": "OK",
  "description": "Products retrieved successfully",
  "payload": [
    {
      "prodId": 1,
      "prodName": "Laptop",
      "prodCode": "PROD001",
      "prodDescription": "High-performance laptop",
      "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
      "prodCost": 999.99
    }
  ],
  "timestamp": "2024-01-15 10:30:00"
}
```

### 3.2 Search by Product Code

**Endpoint:** `GET /api/products/search?productCode=PROD001`

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "statusMessage": "OK",
  "description": "Products retrieved successfully",
  "payload": [
    {
      "prodId": 1,
      "prodName": "Laptop",
      "prodCode": "PROD001",
      "prodDescription": "High-performance laptop",
      "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
      "prodCost": 999.99
    }
  ],
  "timestamp": "2024-01-15 10:30:00"
}
```

### 3.3 Search by Both Product Name and Product Code

**Endpoint:** `GET /api/products/search?productName=Laptop&productCode=PROD`

**Response (200 OK):**
```json
{
  "statusCode": 200,
  "statusMessage": "OK",
  "description": "Products retrieved successfully",
  "payload": [
    {
      "prodId": 1,
      "prodName": "Laptop",
      "prodCode": "PROD001",
      "prodDescription": "High-performance laptop",
      "prodImage": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==",
      "prodCost": 999.99
    }
  ],
  "timestamp": "2024-01-15 10:30:00"
}
```

---

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "statusCode": 400,
  "statusMessage": "Bad Request",
  "description": "Validation failed",
  "payload": null,
  "timestamp": "2024-01-15 10:30:00"
}
```

### Product Not Found (400 Bad Request)
```json
{
  "statusCode": 400,
  "statusMessage": "Bad Request",
  "description": "Product not found with ID: 999",
  "payload": null,
  "timestamp": "2024-01-15 10:30:00"
}
```

### Duplicate Product Code (400 Bad Request)
```json
{
  "statusCode": 400,
  "statusMessage": "Bad Request",
  "description": "Product with code PROD001 already exists",
  "payload": null,
  "timestamp": "2024-01-15 10:30:00"
}
```

### Missing Search Parameters (400 Bad Request)
```json
{
  "statusCode": 400,
  "statusMessage": "Bad Request",
  "description": "At least one search parameter (productName or productCode) is required",
  "payload": null,
  "timestamp": "2024-01-15 10:30:00"
}
```

---

## Postman Collection JSON

You can import this into Postman:

```json
{
  "info": {
    "name": "Product Service API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Add Product",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"prodName\": \"Laptop\",\n  \"prodCode\": \"PROD001\",\n  \"prodDescription\": \"High-performance laptop\",\n  \"prodImage\": \"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==\",\n  \"prodCost\": 999.99\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/products/add",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "products", "add"]
        }
      }
    },
    {
      "name": "Update Product",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"prodId\": 1,\n  \"prodName\": \"Gaming Laptop\",\n  \"prodCode\": \"PROD001\",\n  \"prodDescription\": \"Updated high-performance gaming laptop\",\n  \"prodImage\": \"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==\",\n  \"prodCost\": 1299.99\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/products/update",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "products", "update"]
        }
      }
    },
    {
      "name": "Search by Product Name",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/products/search?productName=Laptop",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "products", "search"],
          "query": [
            {
              "key": "productName",
              "value": "Laptop"
            }
          ]
        }
      }
    },
    {
      "name": "Search by Product Code",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/products/search?productCode=PROD001",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "products", "search"],
          "query": [
            {
              "key": "productCode",
              "value": "PROD001"
            }
          ]
        }
      }
    },
    {
      "name": "Search by Both",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/products/search?productName=Laptop&productCode=PROD",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "products", "search"],
          "query": [
            {
              "key": "productName",
              "value": "Laptop"
            },
            {
              "key": "productCode",
              "value": "PROD"
            }
          ]
        }
      }
    }
  ]
}
```

---

## Notes

1. **Database Setup**: Make sure MySQL is running and the database `kamesh` exists before starting the application.

2. **Application Port**: The application runs on port 8080 by default.

3. **Validation**: All endpoints include request validation. Invalid requests will return 400 Bad Request with error messages.

4. **Search**: The search endpoint supports partial matching (case-insensitive) for both product name and product code.

5. **Product Code**: Must be unique. Attempting to add a product with an existing code will result in an error.

6. **Product Image**: The `prodImage` field accepts Base64 encoded image data. Make sure to encode your image to Base64 format before sending.

7. **Product Fields**: The product entity contains:
   - `prodId`: Auto-generated unique identifier (Long)
   - `prodName`: Product name (required, max 255 characters)
   - `prodCode`: Product code (required, unique, max 50 characters)
   - `prodDescription`: Product description (optional, TEXT)
   - `prodImage`: Product image as Base64 encoded string (optional, LONGTEXT)
   - `prodCost`: Product cost (required, must be greater than 0, precision: 10, scale: 2)
