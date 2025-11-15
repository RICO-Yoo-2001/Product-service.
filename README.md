# Product Service - REST API

A Spring Boot REST API for managing products with MySQL database integration.

## Features

- Add new products
- Update existing products
- Search products by product name and/or product code
- Swagger UI for API documentation
- Log4j2 for logging
- Input validation
- Global exception handling

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Web** - REST API
- **Spring Data JPA** - Database operations
- **MySQL** - Database
- **Lombok** - Reduce boilerplate code
- **Swagger UI** - API documentation
- **Log4j2** - Logging framework
- **Java 17**

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, etc.)

## Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE kamesh;
```

2. Update database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/kamesh
spring.datasource.username=root
spring.datasource.password=
```

## Running the Application

1. **Clone/Navigate to the project directory:**
```bash
cd product_adi
```

2. **Build the project:**
```bash
mvn clean install
```

3. **Run the application:**
```bash
mvn spring-boot:run
```

Or run the main class from your IDE.

4. **Application will start on:** `http://localhost:8080`

## API Endpoints

### 1. Add Product

#### Option A: JSON with Base64 Image
- **POST** `/api/products/add`
- Creates a new product with Base64 encoded image in JSON body
- Request body example:
```json
{
  "prodName": "Laptop",
  "prodCode": "PROD001",
  "prodDescription": "High-performance laptop",
  "prodImage": "base64EncodedImageString",
  "prodCost": 999.99
}
```

#### Option B: File Upload (Multipart)
- **POST** `/api/products/add-with-image`
- Creates a new product with direct image file upload
- Content-Type: `multipart/form-data`
- Form fields:
  - `prodName` (required)
  - `prodCode` (required)
  - `prodDescription` (optional)
  - `prodImage` (optional) - Image file (JPEG, PNG, GIF, WEBP - max 5MB)
  - `prodCost` (required)

### 2. Update Product

#### Option A: JSON with Base64 Image
- **POST** `/api/products/update`
- Updates an existing product with Base64 encoded image in JSON body
- **Important:** The `prodId` in the request body is used only to identify which product to update. The product ID cannot be changed and will always remain the same.
- Request body example:
```json
{
  "prodId": 1,
  "prodName": "Gaming Laptop",
  "prodCode": "PROD001",
  "prodDescription": "Updated description",
  "prodImage": "base64EncodedImageString",
  "prodCost": 1299.99
}
```

#### Option B: File Upload (Multipart)
- **POST** `/api/products/update-with-image`
- Updates an existing product with direct image file upload
- Content-Type: `multipart/form-data`
- Form fields:
  - `prodId` (required)
  - `prodName` (required)
  - `prodCode` (required)
  - `prodDescription` (optional)
  - `prodImage` (optional) - Image file (JPEG, PNG, GIF, WEBP - max 5MB)
  - `prodCost` (required)

### 3. Search Products
- **GET** `/api/products/search`
- Search by product name and/or product code (partial match, case-insensitive)
- Query parameters:
  - `productName` (optional)
  - `productCode` (optional)
- At least one parameter is required

## API Documentation

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs (JSON):** http://localhost:8080/api-docs

## Postman Testing

See `POSTMAN_ENDPOINTS.md` for detailed Postman endpoint documentation with request/response examples.

## Project Structure

```
src/main/java/com/service/packages/
├── config/          # Configuration classes (Swagger)
├── controller/      # REST controllers
├── dto/
│   ├── request/     # Request DTOs
│   └── response/    # Response DTOs
├── entity/          # JPA entities
├── exception/       # Exception handlers
├── repository/      # JPA repositories
├── service/
│   └── impl/       # Service implementations
└── [Main Application Class]
```

## Logging

Logs are written to:
- Console output
- File: `logs/product-service.log`

## Response Format

All API responses follow this format:

```json
{
  "statusCode": 200,
  "statusMessage": "OK",
  "description": "Operation message",
  "payload": { ... },
  "timestamp": "2024-01-15 10:30:00"
}
```

## Product Entity Structure

The product entity contains the following fields:

- **prodId** (Long): Auto-generated unique identifier. **This field cannot be changed after creation.**
- **prodName** (String): Product name (required, max 255 characters)
- **prodCode** (String): Product code (required, unique, max 50 characters)
- **prodDescription** (String): Product description (optional, TEXT)
- **prodImage** (String): Product image as Base64 encoded string (optional, LONGTEXT)
- **prodCost** (BigDecimal): Product cost (required, must be greater than 0, precision: 10, scale: 2)

**Important:** When updating a product, the `prodId` is used only to identify which product to update. The product ID itself cannot be modified and will always remain the same as the original ID.

## Validation

- **Product name**: Required, max 255 characters
- **Product code**: Required, max 50 characters, must be unique
- **Product cost**: Required, must be greater than 0, max 8 integer digits and 2 decimal places
- **Product description**: Optional
- **Product image**: Optional, Base64 encoded string

## Error Handling

The application includes global exception handling for:
- Validation errors
- Resource not found errors
- Duplicate product code errors
- General runtime exceptions

## Image Handling

The API supports two methods for image upload:

### Method 1: Base64 Encoded String (JSON)
1. Encode your image file to Base64 format
2. Include the Base64 string in the JSON request body
3. Use endpoints: `/api/products/add` or `/api/products/update`

Example: Convert an image to Base64 using command line:
```bash
base64 -i image.jpg -o encoded.txt
```

### Method 2: Direct File Upload (Multipart)
1. Upload the image file directly using `multipart/form-data`
2. The API automatically converts the image to Base64
3. Use endpoints: `/api/products/add-with-image` or `/api/products/update-with-image`

**Image Requirements:**
- **Supported formats**: JPEG, PNG, GIF, WEBP
- **Maximum file size**: 5MB
- **Storage**: Images are stored as Base64 encoded strings in the database (LONGTEXT field)

**Note:** Both methods store the image as Base64 in the database. The file upload method is more convenient as it handles the Base64 conversion automatically.
