# Frontend Folder Structure Reference

## Correct Folder Paths

The frontend is a **Next.js** application located in:
```
/frontend/ui/
```

**Important:** 
- The folder name is `frontend` (lowercase), not `Frontend` (capital F)
- This is a Next.js 16 project with TypeScript
- The app directory structure uses Next.js App Router

## Directory Structure

```
ecommerce-maven/
├── frontend/              ← Frontend root (lowercase)
│   └── ui/                ← Next.js 16 application (App Router)
│       ├── app/           ← Next.js App Router directory
│       │   ├── login/
│       │   │   └── page.tsx
│       │   ├── dashboard/
│       │   │   └── page.tsx
│       │   ├── profile/
│       │   │   └── page.tsx
│       │   ├── layout.tsx
│       │   └── page.tsx
│       ├── components/    ← React components
│       │   ├── navbar.tsx
│       │   └── signOutButton.tsx
│       ├── lib/           ← Utility libraries
│       │   └── firebaseClient.ts
│       ├── package.json   ← Next.js 16.0.3, React 19, TypeScript
│       ├── tsconfig.json  ← TypeScript config with path aliases
│       └── next.config.ts ← Next.js configuration
└── src/                   ← Backend (Spring Boot)
```

## Common Commands

### Navigate to Frontend
```bash
cd frontend/ui
```

### Install Dependencies
```bash
cd frontend/ui
npm install
```

### Run Development Server
```bash
cd frontend/ui
npm run dev
```

## Environment Variables

Create `.env.local` in:
```
frontend/ui/.env.local
```

## Code References

All imports in frontend code use TypeScript path aliases (configured in `tsconfig.json`):
- `@/lib/firebaseClient` → `frontend/ui/lib/firebaseClient.ts`
- `@/components/...` → `frontend/ui/components/...`
- `@/app/...` → `frontend/ui/app/...`

**Next.js App Router:**
- Pages are in `app/` directory
- Each route has a `page.tsx` file
- Layout is in `app/layout.tsx`

## Backend API Calls

Frontend makes API calls to:
- Local: `http://localhost:8080/api/...`
- Production: Update to your AWS backend URL

## Verification

To verify the correct path, run:
```bash
cd /Users/kkiiru/Downloads/ecommerce-maven
ls -la | grep frontend
```

Should show: `drwxr-xr-x frontend`

