# Empower Gym – Android App

A Jetpack Compose (Material 3) Android app implementing the Empower Gym UI mockup:
Dashboard, Register Member, Members Directory, Member Details, Fee Update, and
WhatsApp confirmation screens.

## What's included
- **Dashboard**: stats cards, quick actions, recent activity
- **Register Member**: auto-generated Member ID, membership type, and **Package**
  selector — now with **Monthly / Quarterly / Half Yearly / Yearly** options
- **Members Directory**: search + member cards
- **Member Details**: profile info + payment summary
- **Fee Update**: period grid (adapts to package — months, quarters, half-years, or year)
- **WhatsApp Confirmation**: notify member/owner toggle

Color theme matches the spec: Primary Blue `#1565C0`, Secondary Green `#2E7D32`,
background `#F5F7FA`, Material 3 components.

## How to open & run
1. Install **Android Studio** (Koala or newer).
2. Open the `EmpowerGym` folder as an existing project (File → Open).
3. Let Gradle sync (it will download the wrapper automatically).
4. Run on an emulator or device (minSdk 24 / Android 7.0+).

## Notes
- All data is in-memory sample data (`MemberRepository`) — no backend/database yet.
- Camera capture and WhatsApp sending are UI placeholders; wire up
  `CameraX` and the WhatsApp Business/Cloud API when ready for production.
- Swap the default font for **Poppins** by adding the font files under
  `res/font/` and referencing them in `ui/theme/Theme.kt`.
