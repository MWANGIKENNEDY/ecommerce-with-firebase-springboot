# AWS Deployment Guide

This guide will walk you through deploying your Spring Boot ecommerce application to AWS.

## Prerequisites

1. AWS Account
2. AWS CLI installed and configured
3. Docker (for containerization)
4. MySQL database (we'll use AWS RDS)

## Step 1: Create AWS RDS MySQL Database

### Option A: Using AWS Console

1. **Go to AWS RDS Console**

   - Navigate to: https://console.aws.amazon.com/rds/
   - Click "Create database"

2. **Database Configuration**

   - **Engine**: MySQL
   - **Version**: MySQL 8.0 (or latest)
   - **Template**: Free tier (for testing) or Production
   - **DB Instance Identifier**: `ecommerce-db`
   - **Master Username**: `admin` (or your preferred username) ← **This is DB_USERNAME**
   - **Master Password**: Create a strong password ← **This is DB_PASSWORD**
   - **DB Name**: `ecommerce` ← **This is DB_NAME**
   - **Instance Class**: db.t3.micro (free tier) or db.t3.small (production)
   - **Storage**: 20 GB (minimum)

3. **Network & Security**

   - **VPC**: Use default VPC or create new one
   - **Public Access**: Yes (for easier setup) or No (more secure)
   - **Security Group**: Create new or use existing
   - **Availability Zone**: Any

4. **Additional Configuration**

   - **Backup**: Enable automated backups
   - **Monitoring**: Optional

5. **Click "Create database"**

6. **After Creation - Find Your Database Endpoint**
   - Once created, click on your database instance
   - Find the **Endpoint** (e.g., `ecommerce-db.xxxxx.us-east-1.rds.amazonaws.com`) ← **This is DB_HOST**
   - Note the **Port** (default is 3306) ← **This is DB_PORT**

### Option B: Using AWS CLI

```bash
aws rds create-db-instance \
    --db-instance-identifier ecommerce-db \
    --db-instance-class db.t3.micro \
    --engine mysql \
    --master-username admin \
    --master-user-password YourSecurePassword123! \
    --allocated-storage 20 \
    --db-name ecommerce \
    --publicly-accessible \
    --storage-type gp2
```

## Step 2: Configure Security Group

1. **Go to RDS Console** → Your database → **Connectivity & security** tab
2. **Click on Security Group** (e.g., `sg-xxxxx`)
3. **Edit Inbound Rules**
   - Add rule:
     - **Type**: MySQL/Aurora
     - **Port**: 3306
     - **Source**:
       - For testing: `0.0.0.0/0` (allows from anywhere)
       - For production: Your application's security group or specific IP

## Step 3: Environment Variables

These are the values you need to set in your deployment environment:

| Variable                        | Where to Find                                  | Example                                          |
| ------------------------------- | ---------------------------------------------- | ------------------------------------------------ |
| `DB_HOST`                       | RDS Console → Database → Endpoint              | `ecommerce-db.xxxxx.us-east-1.rds.amazonaws.com` |
| `DB_PORT`                       | RDS Console → Database → Port                  | `3306`                                           |
| `DB_NAME`                       | The database name you created                  | `ecommerce`                                      |
| `DB_USERNAME`                   | Master username you set during creation        | `admin`                                          |
| `DB_PASSWORD`                   | Master password you set during creation        | `YourSecurePassword123!`                         |
| `FIREBASE_SERVICE_ACCOUNT_JSON` | Your Firebase service account JSON (as string) | `{"type":"service_account",...}`                 |

## Step 4: Deployment Options

### Option A: AWS Elastic Beanstalk (Easiest)

1. **Install EB CLI**

   ```bash
   pip install awsebcli
   ```

2. **Initialize Elastic Beanstalk**

   ```bash
   cd /Users/kkiiru/Downloads/ecommerce-maven
   eb init -p "Docker" ecommerce-app
   ```

3. **Create Environment**

   ```bash
   eb create ecommerce-env
   ```

4. **Set Environment Variables**

   ```bash
   eb setenv \
     DB_HOST=ecommerce-db.xxxxx.us-east-1.rds.amazonaws.com \
     DB_PORT=3306 \
     DB_NAME=ecommerce \
     DB_USERNAME=admin \
     DB_PASSWORD=YourSecurePassword123! \
     SPRING_PROFILES_ACTIVE=prod \
     FIREBASE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
   ```

5. **Deploy**
   ```bash
   eb deploy
   ```

### Option B: AWS ECS (Elastic Container Service)

1. **Build and Push Docker Image**

   ```bash
   # Build image
   docker build -t ecommerce-app .

   # Tag for ECR
   docker tag ecommerce-app:latest <account-id>.dkr.ecr.<region>.amazonaws.com/ecommerce-app:latest

   # Login to ECR
   aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com

   # Push image
   docker push <account-id>.dkr.ecr.<region>.amazonaws.com/ecommerce-app:latest
   ```

2. **Create ECS Task Definition** with environment variables:
   ```json
   {
     "environment": [
       {
         "name": "DB_HOST",
         "value": "ecommerce-db.xxxxx.us-east-1.rds.amazonaws.com"
       },
       { "name": "DB_PORT", "value": "3306" },
       { "name": "DB_NAME", "value": "ecommerce" },
       { "name": "DB_USERNAME", "value": "admin" },
       { "name": "DB_PASSWORD", "value": "YourSecurePassword123!" },
       { "name": "SPRING_PROFILES_ACTIVE", "value": "prod" },
       { "name": "FIREBASE_SERVICE_ACCOUNT_JSON", "value": "..." }
     ]
   }
   ```

### Option C: AWS EC2 (Manual Deployment)

1. **Launch EC2 Instance**

   - AMI: Amazon Linux 2
   - Instance Type: t3.small or larger
   - Security Group: Allow HTTP (80), HTTPS (443), SSH (22)

2. **SSH into Instance**

   ```bash
   ssh -i your-key.pem ec2-user@<ec2-ip>
   ```

3. **Install Java 17 and Docker**

   ```bash
   sudo yum update -y
   sudo yum install java-17-amazon-corretto -y
   sudo yum install docker -y
   sudo service docker start
   sudo usermod -a -G docker ec2-user
   ```

4. **Set Environment Variables**

   ```bash
   export DB_HOST=ecommerce-db.xxxxx.us-east-1.rds.amazonaws.com
   export DB_PORT=3306
   export DB_NAME=ecommerce
   export DB_USERNAME=admin
   export DB_PASSWORD=YourSecurePassword123!
   export SPRING_PROFILES_ACTIVE=prod
   export FIREBASE_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
   ```

5. **Run Docker Container**
   ```bash
   docker run -d \
     -p 8080:8080 \
     -e DB_HOST=$DB_HOST \
     -e DB_PORT=$DB_PORT \
     -e DB_NAME=$DB_NAME \
     -e DB_USERNAME=$DB_USERNAME \
     -e DB_PASSWORD=$DB_PASSWORD \
     -e SPRING_PROFILES_ACTIVE=prod \
     -e FIREBASE_SERVICE_ACCOUNT_JSON="$FIREBASE_SERVICE_ACCOUNT_JSON" \
     ecommerce-app
   ```

## Step 5: Firebase Service Account JSON

### Option 1: Environment Variable (Recommended for Production)

1. **Get your serviceAccountKey.json content**

   ```bash
   cat src/main/resources/serviceAccountKey.json
   ```

2. **Set as environment variable** (escape quotes properly)
   ```bash
   export FIREBASE_SERVICE_ACCOUNT_JSON='{"type":"service_account","project_id":"...","private_key_id":"...","private_key":"...","client_email":"...","client_id":"...","auth_uri":"...","token_uri":"...","auth_provider_x509_cert_url":"...","client_x509_cert_url":"..."}'
   ```

### Option 2: AWS Secrets Manager (More Secure)

1. **Store in AWS Secrets Manager**

   ```bash
   aws secretsmanager create-secret \
     --name ecommerce/firebase-service-account \
     --secret-string file://src/main/resources/serviceAccountKey.json
   ```

2. **Retrieve in application** (requires AWS SDK integration)

## Step 6: Verify Deployment

1. **Check Application Logs**

   ```bash
   # Elastic Beanstalk
   eb logs

   # ECS
   aws logs tail /ecs/ecommerce-app --follow

   # EC2
   docker logs <container-id>
   ```

2. **Test Endpoints**
   ```bash
   curl http://<your-app-url>/api/products
   ```

## Security Best Practices

1. ✅ **Never commit** `serviceAccountKey.json` to Git
2. ✅ Use **AWS Secrets Manager** for sensitive data
3. ✅ Use **RDS in private subnet** (not publicly accessible)
4. ✅ Use **Security Groups** to restrict database access
5. ✅ Enable **SSL/TLS** for database connections
6. ✅ Use **IAM roles** instead of access keys when possible
7. ✅ Enable **CloudWatch** logging and monitoring

## Troubleshooting

### Database Connection Issues

- Check Security Group rules allow connection from your application
- Verify database endpoint and credentials
- Check if database is publicly accessible (if needed)

### Firebase Initialization Issues

- Verify FIREBASE_SERVICE_ACCOUNT_JSON is properly escaped
- Check JSON format is valid
- Ensure Firebase project is active

### Application Won't Start

- Check CloudWatch logs
- Verify all environment variables are set
- Check database connectivity from application server

## Cost Estimation

- **RDS db.t3.micro**: ~$15/month (free tier eligible for 12 months)
- **EC2 t3.small**: ~$15/month
- **Elastic Beanstalk**: Free (you pay for underlying resources)
- **ECS**: Free (you pay for underlying resources)
- **Data Transfer**: ~$0.09/GB

## Next Steps

1. Set up **CloudWatch** alarms for monitoring
2. Configure **Auto Scaling** for high availability
3. Set up **CI/CD** pipeline with GitHub Actions or AWS CodePipeline
4. Configure **Custom Domain** with Route 53
5. Set up **SSL Certificate** with AWS Certificate Manager
