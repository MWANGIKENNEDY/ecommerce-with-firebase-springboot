# User Login Guide - How to Start Shopping

This guide explains how users can log in to your ecommerce system and start shopping.

## 🔐 Authentication System

Your ecommerce application uses **Firebase Authentication** with **Google Sign-In**. This means users can log in using their Google account - no need to create a separate account!

## 📋 Prerequisites

Before users can log in, you need to:

1. **Set up Firebase Authentication** (if not already done)
2. **Configure Firebase in your frontend**
3. **Ensure backend is running**

## 🚀 How Users Log In

### Step 1: Navigate to Login Page

Users can access the login page by:
- Going to `/login` route in your frontend application
- Clicking the "Login" button in the navbar (if not logged in)

### Step 2: Click "Continue with Google"

On the login page, users will see a button:
```
[Continue with Google]
```

### Step 3: Google Sign-In Popup

When users click the button:
1. A Google sign-in popup will appear
2. User selects their Google account
3. User grants permission to your app
4. Firebase authenticates the user

### Step 4: Automatic Backend Registration

After Google authentication:
1. Frontend gets a Firebase ID token
2. Frontend sends token to backend: `POST /api/auth/login`
3. Backend verifies the token and creates/updates user in database
4. User is automatically registered in your system
5. User is redirected to `/dashboard`

## 🔄 Complete Login Flow

```
User → Login Page → Click "Continue with Google" 
  → Google Popup → Select Account 
  → Firebase Authentication 
  → Get ID Token 
  → Send to Backend (/api/auth/login) 
  → Backend Creates/Updates User 
  → Redirect to Dashboard 
  → User Can Now Shop! 🛒
```

## 🛒 What Users Can Do After Login

Once logged in, users can:

1. **Browse Products**
   - View all products: `GET /api/products`
   - View product details: `GET /api/products/{id}`

2. **Manage Cart** (requires authentication)
   - View cart: `GET /api/cart`
   - Add items: `POST /api/cart/items`
   - Remove items: `DELETE /api/cart/items/{cartItemId}`

3. **Checkout** (requires authentication)
   - Create order: `POST /api/orders/checkout`
   - View orders: `GET /api/orders`
   - View order details: `GET /api/orders/{orderId}`

## 🔧 Setup Instructions for Developers

### 1. Firebase Console Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project (or create new one)
3. Go to **Authentication** → **Sign-in method**
4. Enable **Google** as a sign-in provider
5. Add authorized domains (your frontend domain)

### 2. Frontend Environment Variables

Create a `.env.local` file in `frontend/ui/`:

```env
NEXT_PUBLIC_FIREBASE_API_KEY=your-api-key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your-project-id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=your-sender-id
NEXT_PUBLIC_FIREBASE_APP_ID=your-app-id
```

**Where to find these values:**
- Firebase Console → Project Settings → General → Your apps → Web app config

### 3. Backend Configuration

Ensure `serviceAccountKey.json` is configured:
- File location: `src/main/resources/serviceAccountKey.json`
- This file is used to verify Firebase tokens on the backend

### 4. Update Frontend Backend URL

In `frontend/ui/app/login/page.tsx`, update the backend URL:

```typescript
// For local development
const res = await fetch("http://localhost:8080/api/auth/login", {
  method: "POST",
  headers: {
    Authorization: `Bearer ${token}`,
  },
});

// For production (update to your AWS backend URL)
const res = await fetch("https://your-backend-url.com/api/auth/login", {
  method: "POST",
  headers: {
    Authorization: `Bearer ${token}`,
  },
});
```

## 🧪 Testing the Login Flow

### 1. Start Backend
```bash
cd /Users/kkiiru/Downloads/ecommerce-maven
mvn spring-boot:run
```

### 2. Start Frontend
```bash
cd frontend/ui
npm install
npm run dev
```

**Note:** Make sure you're in the `frontend/ui` directory (not `Frontend` or `Frontend/ui`).

### 3. Test Login
1. Open browser: `http://localhost:3000/login`
2. Click "Continue with Google"
3. Select a Google account
4. Should redirect to `/dashboard`

## 🔍 Troubleshooting

### Issue: "Firebase: Error (auth/unauthorized-domain)"

**Solution:**
- Go to Firebase Console → Authentication → Settings
- Add your domain to "Authorized domains"
- For local testing, add `localhost`

### Issue: "Backend login failed"

**Solution:**
- Check backend is running on port 8080
- Verify `serviceAccountKey.json` is in `src/main/resources/`
- Check backend logs for errors
- Verify Firebase project ID matches in both frontend and backend

### Issue: "Cannot read properties of undefined"

**Solution:**
- Ensure all Firebase environment variables are set in `.env.local`
- Restart Next.js dev server after adding environment variables

### Issue: Google popup blocked

**Solution:**
- Allow popups for your domain
- Check browser console for errors
- Try in incognito mode to rule out extension issues

## 📱 User Experience Flow

1. **First Visit:**
   - User lands on homepage
   - Clicks "Login" in navbar
   - Redirected to `/login`
   - Clicks "Continue with Google"
   - Selects Google account
   - **User account created automatically in database**
   - Redirected to dashboard

2. **Returning User:**
   - User lands on homepage
   - Clicks "Login" in navbar
   - Clicks "Continue with Google"
   - Selects same Google account
   - **Existing user account updated**
   - Redirected to dashboard

3. **Shopping Flow:**
   - User browses products
   - Adds items to cart
   - Views cart
   - Checks out (creates order)
   - Views order history

## 🔐 Security Features

- ✅ **Token-based authentication** - Firebase ID tokens
- ✅ **Automatic token refresh** - Firebase handles token expiration
- ✅ **Secure backend verification** - Backend verifies all tokens
- ✅ **Protected routes** - Cart and orders require authentication
- ✅ **No password storage** - Google handles authentication

## 📝 API Endpoints Reference

### Public Endpoints (No Auth Required)
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product details

### Protected Endpoints (Auth Required)
- `POST /api/auth/login` - Login/Register user
- `GET /api/cart` - Get user's cart
- `POST /api/cart/items` - Add item to cart
- `DELETE /api/cart/items/{id}` - Remove item from cart
- `POST /api/orders/checkout` - Create order from cart
- `GET /api/orders` - Get user's orders
- `GET /api/orders/{id}` - Get order details

## 🎯 Quick Start Checklist

- [ ] Firebase project created
- [ ] Google sign-in enabled in Firebase
- [ ] Firebase environment variables set in frontend
- [ ] `serviceAccountKey.json` in backend resources
- [ ] Backend running on port 8080
- [ ] Frontend running on port 3000
- [ ] Test login flow end-to-end
- [ ] Verify user created in database after login

## 🚀 Production Deployment

When deploying to production:

1. **Update frontend backend URL** to your AWS backend URL
2. **Add production domain** to Firebase authorized domains
3. **Use environment variables** for Firebase config (not hardcoded)
4. **Enable HTTPS** for secure token transmission
5. **Configure CORS** properly on backend

---

**That's it!** Users can now log in with Google and start shopping! 🎉

