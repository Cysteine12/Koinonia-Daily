const ua = process.env.npm_config_user_agent;

if (!ua || !ua.startsWith('pnpm/')) {
  console.error("This project uses pnpm. Please run 'pnpm install'.");
  process.exit(1);
}
