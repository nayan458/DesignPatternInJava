import { AppBar, Toolbar, Typography } from "@mui/material";
import type React from "react";
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';


interface AppBarNavigationInterface {
        drawerWidth: number, 
        handleDrawerToggle: () => void
}

const AppBarNavigation: React.FC<AppBarNavigationInterface> =({drawerWidth, handleDrawerToggle})=>{
    return (
            <AppBar
                position="fixed"
                sx={{
                    width: { sm: `calc(100% - ${drawerWidth}px)` },
                    ml: { sm: `${drawerWidth}px` },
                }}
                >
                <Toolbar>
                    <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    edge="start"
                    onClick={handleDrawerToggle}
                    sx={{ mr: 2, display: { sm: 'none' } }}
                    >
                    <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" noWrap component="div">
                    Auto Generated Documentation
                    </Typography>
                </Toolbar>
            </AppBar>
    )
}

export default AppBarNavigation