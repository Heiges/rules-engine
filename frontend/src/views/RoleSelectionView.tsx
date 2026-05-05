import { useNavigate } from 'react-router-dom'
import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import './HomeView.css'

export function RoleSelectionView() {
  const navigate = useNavigate()
  const { setRole } = useRuleset()

  return (
    <div className="home-view">
      <h1>Rules Engine</h1>
      <div className="tile-grid">
        <Tile
          id="spielleiter"
          name="Spielleiter"
          description="Ich will Regeln anpassen und meine Spielwelt definieren"
          onClick={() => { setRole('spielleiter'); navigate('/home') }}
        />
        <Tile
          id="spieler"
          name="Spieler"
          description="Spass, ich will Spass"
          onClick={() => { setRole('spieler'); navigate('/player') }}
        />
      </div>
    </div>
  )
}
