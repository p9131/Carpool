# Carpool Application 🚗💨  

A **secure**, **user-friendly** rideshare app designed exclusively for the **Faculty of Engineering Community at Ain Shams University**.  

---

## 📌 Table of Contents  
- [Introduction](#-introduction)  
- [Key Features](#-key-features)  
- [UI Design](#-ui-design)  
- [Database Structure](#-database-structure)  
- [Test Credentials](#-test-credentials)  
- [Installation](#-installation)  
- [Tech Stack](#-tech-stack)  
- [License](#-license)  

---

## 🌟 Introduction  
The **Carpool Application** is a **closed-community rideshare platform** tailored for students and staff of Ain Shams University’s Faculty of Engineering.  

🔐 **Secure Authentication**:  
- Only `@eng.asu.edu.eg` emails are allowed.  
- Ensures a **trusted network** of verified users.  

🛠 **Key Functionalities**:  
- **Route Listing** (to/from Ain Shams Campus).  
- **Cart & Payment System**.  
- **Order History & Tracking**.  
- **Driver Web Portal** (for trip management).  

📱 **Optimized UX**:  
- **Portrait-mode only** (locked for consistency).  
- **Offline access** (cached user profiles via Room DB).  

---

## 🚀 Key Features  

| Feature | Description |  
|---------|------------|  
| **Secure Login** | Firebase Auth + `@eng.asu.edu.eg` validation |  
| **Route Booking** | Filterable list of morning/afternoon rides |  
| **Reservation Rules** | Morning rides: Book by **10 PM (previous day)**<br>Afternoon rides: Book by **4:30 PM (same day)** |  
| **Order Tracking** | Real-time status updates (Confirmed/Pending) |  
| **Driver Portal** | WebView for drivers to manage trips |  
| **Offline Support** | Room DB caches user profiles |  

---

## 🎨 UI Design  
**Minimalist & Intuitive** with a **gold-themed** palette.  

### Screens:  
1. **Login/Signup**  
   - Email validation for `@eng.asu.edu.eg`.  
2. **Route Listing**  
   - RecyclerView with card-based UI.  
3. **Cart & Checkout**  
   - Review rides before payment.  
4. **Order History**  
   - Track past/current rides.  

---

## 🗃 Database Structure  
**Hybrid Storage:** Firebase (Cloud) + Room (Local).  

### 🔥 **Firebase Firestore**  
- Collections:  
  - `users` (Student/Driver profiles).  
  - `drivers` (Driver-specific data).  
  - `routes` (Trip details + user request status).  

### 📱 **Room Database**  
- Tables:  
  - `user` (Profile data).  
  - `password` (Hashed passwords for offline login).  

---

## 🔑 Test Credentials  
Use these accounts for testing:  

| Role | Email | Password |  
|------|-------|----------|  
| **Driver** | `test@gmail.com` | `test@1` |  
| **User** | `19p0000@eng.asu.edu.eg` | `test@2` |  

---

## ⚙ Installation  

1. Clone the repo:  
   ```bash
   git clone https://github.com/[username]/carpool-app.git
   ```
2. Open in Android Studio  
3. Add your `google-services.json` (Firebase config)  
4. Run on an emulator/device (API 24+)  

---

## 🛠 Tech Stack  

- **Frontend**: Android (Kotlin)  
- **Backend**: Firebase Auth + Firestore  
- **Local DB**: Room  
- **Driver Portal**: HTML/CSS/JS (WebView)  

---

## 📜 License  

© Abdel Rahman Mohamed Salah

---

## 🚀 Ready to ride? Clone and explore!

```bash
git clone https://github.com/p9131/Carpool.git
```

Made for ASU Engineering Community 🎓







