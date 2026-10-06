# Smart Hospital Queue – Frontend

A responsive **React.js** dashboard for the **Smart Predictive Hospital Queue and Appointment Management System**. It lets hospital staff manage patients, doctors, and departments, and monitor a live priority-based patient queue.

> **Backend repo (Spring Boot + MySQL):** [smart-hospital](https://github.com/Akshayadevi05/smart-hospital)
> **Live demo:** `<add-link>`

---

## Features

- Dedicated views for **Patients, Doctors, Departments, and Queue**
- Add, view, edit, and delete records (full CRUD) connected to the REST API
- Queue view that shows patients by urgency: `NORMAL`, `PRIORITY`, `EMERGENCY`
- Queue status updates: `WAITING → SERVING → COMPLETED / CANCELLED`
- Responsive layout for desktop and mobile
- Error and validation messages shown from API responses

## Tech Stack

| Area | Technology |
|---|---|
| Library | React.js |
| Build tool | Vite |
| Styling | HTML5, CSS3, Bootstrap |
| Language | JavaScript |
| API | REST (Spring Boot backend) |
| Tools | Git, GitHub, VS Code |

## Screenshots

> Add 2–3 screenshots in a `/screenshots` folder and link them here.

| Dashboard | Queue |
|---|---|
| `![Dashboard](screenshots/dashboard.png)` | `![Queue](screenshots/queue.png)` |

## Getting Started

### Prerequisites
- Node.js 18+ and npm
- The [backend](https://github.com/Akshayadevi05/smart-hospital) running locally or deployed

### 1. Clone the repo
```bash
git clone https://github.com/Akshayadevi05/smart-hospital-queue.git
cd smart-hospital-queue
```

### 2. Install dependencies
```bash
npm install
```

### 3. Set the API URL
Create a `.env` file in the project root:

```bash
VITE_API_BASE_URL=http://localhost:8080
```

> Make sure your code reads this value with `import.meta.env.VITE_API_BASE_URL` instead of a hard-coded URL.

### 4. Run the app
```bash
npm run dev
```
The app runs at `http://localhost:5173`.

### 5. Build for production
```bash
npm run build
```

## Deployment

Deployed on **Vercel**. Set `VITE_API_BASE_URL` in the Vercel project settings to your hosted backend URL.

## Project Structure

```
src/
├── components/   # Reusable UI components
├── pages/        # Patients, Doctors, Departments, Queue views
├── services/     # API calls to the backend
└── App.jsx
```

> Update this tree to match your actual folders.

## Future Improvements

- Login with role-based access
- Auto-refresh or WebSocket updates for the live queue
- Appointment booking screen

## Author

**Akshaya Devi** – Java Full Stack Developer
[LinkedIn](https://linkedin.com/in/akshayadevimuthukumaran) | [Portfolio](https://akshayadevim-portfolio.vercel.app) | [GitHub](https://github.com/Akshayadevi05)
