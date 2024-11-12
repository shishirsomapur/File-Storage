# File-Storage

This project provides a REST API built with Spring Boot for searching and downloading files from a user-specific folder stored in an S3 bucket. 

## Features

- **Search File**: Search for a file in the user-specific folder on S3.
- **Download File**: Download a file from the user-specific folder.

## Prerequisites

1. **AWS S3 Bucket**: You must create an S3 bucket in your AWS account.
2. **IAM User**: Create an IAM user with access to the S3 bucket.
3. **AWS SDK Configuration**: Add your AWS credentials in `application.properties`.

## Setup Instructions

1. **Create an S3 Bucket:**
   - Log in to your AWS account.
   - Go to **S3** and create a new bucket (e.g., `user-storage-bucket`).
   - Ensure that you allow public access or configure the access control as needed.

2. **Create an IAM User:**
   - Go to **IAM** in AWS.
   - Create a new user and provide it with **Programmatic Access**.
   - Attach the **AmazonS3FullAccess** policy to this user.
   - Copy the **Access Key ID** and **Secret Access Key** for the user.

3. **Configure AWS Credentials in `application.properties`:**
   - In the `src/main/resources/application.properties` file, add your AWS credentials:

   ```properties
   aws.access.key.id=your_aws_access_key_id
   aws.secret.access.key=your_aws_secret_access_key
   aws.region=your_aws_region

